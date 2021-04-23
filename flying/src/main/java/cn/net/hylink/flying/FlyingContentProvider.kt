package cn.net.hylink.flying

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import cn.net.hylink.flying.constant.Constant

/**
 * @ClassName FlyingContentProvider
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/21 16:34
 * @Version 1.0
 */
class FlyingContentProvider: ContentProvider() {

    companion object {
        val TAG = Constant.PREFIX + FlyingContentProvider::class.simpleName
    }

    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        TODO("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }
}