package cn.net.hylink.flying.core

import android.database.Cursor
import android.os.Bundle
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.log.FlyingLog
import java.lang.reflect.Method

/**
 * @ClassName RealCall
 * @Description 请求
 * @Author haosiyuan
 * @Date 2021/4/27 14:35
 * @Version 1.0
 */
class RealCall constructor(
        private val flyingMessage: FlyingMessage
) {

    companion object {
        private val TAG = RealCall::class.java.simpleName
    }

    fun execute(method: Method, bundle: Bundle): Bundle? {
        return try {
            FlyingLog.d(TAG, "uri: $flyingMessage.base, contentValues: $bundle, method: ${method.name}")
            flyingMessage.mContextGobal.contentResolver.call(flyingMessage.base, method.name, null, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun execute(router: String, bundle: Bundle): Bundle? {
        return try {
            flyingMessage.run {
                val uri = this.base.buildUpon()
                        .appendPath(Constant.FLY_PATH_START).appendPath(
                        Constant.FLY_PATH_SEGMENT_ROUTE)
                        .appendPath(router).build()
                FlyingLog.d(TAG, "uri: $uri, contentValues: $bundle")
                this.mContextGobal.contentResolver.call(uri, "", "", bundle)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun execute(method: Method, service: Class<*>, contentValues: Array<String>): Cursor? {
        return try {
            flyingMessage.run {
                val uri = this.base.buildUpon()
                        .appendPath(Constant.FLY_PATH_START).appendPath(
                        Constant.FLY_PATH_SEGMENT_METHOD)
                        .appendPath(method.name).appendQueryParameter(Constant.FLY_KEY_CLASS, service.name).build()
                FlyingLog.d(TAG, "uri: $flyingMessage.base, contentValues: $contentValues, method: ${method.name}")
                flyingMessage.mContextGobal.contentResolver.query(uri, arrayOfNulls(0), "", contentValues, "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}