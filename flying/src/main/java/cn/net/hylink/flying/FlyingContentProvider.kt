package cn.net.hylink.flying

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.Server
import cn.net.hylink.flying.util.NotFoundRouteException

/**
 * @ClassName FlyingContentProvider
 * @Description 服务端接收
 * @Author haosiyuan
 * @Date 2021/4/21 16:34
 * @Version 1.0
 */
class FlyingContentProvider: ContentProvider() {

    companion object {
        val TAG = Constant.PREFIX + FlyingContentProvider::class.simpleName
    }

    override fun onCreate(): Boolean = true

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        TODO("Not yet implemented")
    }

    override fun call(method: String, arg: String?, requestBundle: Bundle?): Bundle? {
        if (requestBundle == null) return null
        val responseBundle = Bundle()
        try {
            callingPackage?.let {
                requestBundle.putString(Constant.FLY_KEY_CALLING_PACKAGE, it)
            }

            Server.INSTANCE.dispatch(method, requestBundle, responseBundle)
        } catch (e: NoSuchMethodException) {
            requestBundle.putInt(Constant.FLY_KEY_RESPONSE_CODE, Constant.ResponseCode.FLY_RESPONSE_RESULT_NO_SUCH_METHOD);
        } catch (e: NotFoundRouteException) {
            requestBundle.putInt(Constant.FLY_KEY_RESPONSE_CODE, Constant.ResponseCode.FLY_RESPONSE_RESULT_NOT_FOUND_ROUTE);
        } catch (e: IllegalAccessException) {
            requestBundle.putInt(Constant.FLY_KEY_RESPONSE_CODE, Constant.ResponseCode.FLY_RESPONSE_RESULT_ILLEGAL_ACCESS);
        } catch (e: ClassNotFoundException) {
            requestBundle.putInt(Constant.FLY_KEY_RESPONSE_CODE, Constant.ResponseCode.FLY_RESPONSE_RESULT_LOST_CLASS);
        } catch (e: Exception) {
            requestBundle.putInt(Constant.FLY_KEY_RESPONSE_CODE, Constant.ResponseCode.FLY_RESPONSE_RESULT_REMOTE_EXCEPTION);
        }

        return responseBundle
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = uri

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = uri.toString()

    override fun <T : Any?> openPipeHelper(uri: Uri, mimeType: String, opts: Bundle?, args: T?, func: PipeDataWriter<T>): ParcelFileDescriptor {
        return super.openPipeHelper(uri, mimeType, opts, args, func)
    }
}