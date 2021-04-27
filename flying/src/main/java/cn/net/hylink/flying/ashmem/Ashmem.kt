package cn.net.hylink.flying.ashmem

import android.annotation.SuppressLint
import android.os.MemoryFile
import android.os.ParcelFileDescriptor
import java.io.FileDescriptor
import java.lang.Exception
import java.util.*

/**
 * @ClassName Ashmem
 * @Description 用匿名共享内存传输
 * @Author haosiyuan
 * @Date 2021/4/27 16:11
 * @Version 1.0
 */
object Ashmem {

    @SuppressLint("DiscouragedPrivateApi")
    fun byteArrayToFileDescriptor(array: ByteArray): ParcelFileDescriptor? {
        return try {
            MemoryFile(UUID.randomUUID().toString(), array.size).run {
                this.writeBytes(array, 0,0,array.size)
                val method = MemoryFile::class.java.getDeclaredMethod("getFileDescriptor")
                val parcelFileDescriptor = ParcelFileDescriptor
                        .dup(method.invoke(this) as FileDescriptor)
                this.close()
                parcelFileDescriptor
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}