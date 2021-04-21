package cn.net.hylink.flying.queue

import android.os.Handler
import android.os.SystemClock
import kotlin.Exception

/**
 * @ClassName BlockingRunnable
 * @Description 阻塞式 runnable
 * @Author haosiyuan
 * @Date 2021/4/20 17:51
 * @Version 1.0
 */
class BlockingRunnable constructor(private val mTask: Runnable): Runnable {

    /**
     * 是否执行
     */
    private var mDone: Boolean = false

    private val lock = java.lang.Object()

    override fun run() {
        try {
            mTask.run()
        } finally {
            synchronized(this) {
                mDone = true
                //释放锁
                lock.notifyAll()
            }
        }
    }

    fun postAndWait(handler: Handler, timeout: Long): Boolean {
        if (!handler.post(this)) {
            return false
        }

        synchronized(this) {
            if (timeout > 0) {
                val expirationTime = SystemClock.uptimeMillis() + timeout
                while (!mDone) {
                    //未执行
                    val delay = expirationTime - SystemClock.uptimeMillis()
                    if (delay <= 0) {
                        return false
                    }

                    try {
                        lock.wait(delay)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                while (!mDone) {
                    try {
                        lock.wait()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        return true
    }
}