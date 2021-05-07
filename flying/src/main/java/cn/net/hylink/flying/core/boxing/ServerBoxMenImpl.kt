package cn.net.hylink.flying.core.boxing

import android.os.Bundle
import android.os.ParcelFileDescriptor
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.interfaces.IServerBoxMen
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * @ClassName ServerBoxMenImpl
 * @Description 服务端解析
 * @Author haosiyuan
 * @Date 2021/4/30 11:32
 * @Version 1.0
 */
class ServerBoxMenImpl: IServerBoxMen<Bundle> {

    override fun unboxing(bundle: Bundle): Array<Any?> {
        return bundle.let {
            val length = it.getInt(Constant.FLY_KEY_LENGTH)
            val values = arrayOfNulls<Any>(length)

            for (i in 0 until length) {
                val index = String.format(Locale.ENGLISH, Constant.FLY_KEY_INDEX, i)
                values[i] = parcelableToValue(index, bundle)
            }
            values
        }
    }

    private fun parcelableToValue(index: String, bundle: Bundle): Any? {
        val arg = bundle[index]
        if (arg is ParcelFileDescriptor) {
            val fileDescriptor = arg.fileDescriptor
            val fileInputStream = FileInputStream(fileDescriptor)
            val arrayLength = bundle.getInt(Constant.FLY_KEY_ARRAY_LENGTH)
            val bytes = ByteArray(arrayLength)
            try {
                fileInputStream.read(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return bytes
            }
        }
        return arg
    }

    override fun boxing(requestBundle: Bundle?, responseBundle: Bundle?, value: Any?) {
    }

}