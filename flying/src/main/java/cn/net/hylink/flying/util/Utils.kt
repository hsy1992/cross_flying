package cn.net.hylink.flying.util

import android.os.Bundle
import android.os.Parcelable
import cn.net.hylink.flying.ashmem.Ashmem
import java.io.Serializable

/**
 * @ClassName Utils
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/25 9:06
 * @Version 1.0
 */
object Utils {

    /**
     * 检查参数是否符合要求
     */
    fun checkClassAccord(clazz: Class<*>): Boolean {
        return when {
            Int::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Double::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Long::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Short::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Float::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Byte::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Boolean::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    Char::class.javaPrimitiveType!!.isAssignableFrom(clazz) ||
                    ByteArray::class.java.isAssignableFrom(clazz) ||
                    Bundle::class.java.isAssignableFrom(clazz) -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun checkClassAccordReturn(returnType: Class<*>): Boolean {
        if (Void::class.javaPrimitiveType!!.isAssignableFrom(returnType)) {
            return true
        }
        return checkClassAccord(returnType)
    }

    fun convert(key: String, bundle: Bundle, arg: Any) {
        when (arg) {
            is Int -> bundle.putInt(key, arg)
            is Double -> bundle.putDouble(key, arg)
            is Long -> bundle.putLong(key, arg)
            is Short -> bundle.putShort(key, arg)
            is Float -> bundle.putFloat(key, arg)
            is Byte -> bundle.putByte(key, arg)
            is Boolean -> bundle.putBoolean(key, arg)
            is ByteArray -> bundle.putParcelable(key, Ashmem.byteArrayToFileDescriptor(arg))
            is String -> bundle.putString(key, arg)
            is Parcelable -> bundle.putParcelable(key, arg)
            is Serializable -> bundle.putSerializable(key, arg)
        }
    }
}