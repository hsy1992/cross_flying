package cn.net.hylink.flying.queue

import android.os.HandlerThread
import android.os.Looper
import android.os.Process

/**
 * @ClassName DispatchThread
 * @Description 创建分发线程
 * @Author haosiyuan
 * @Date 2021/4/21 10:16
 * @Version 1.0
 */
class DispatchThread {

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = DispatchThread()
    }

    fun create(name: String): Dispatch = create(name, Process.THREAD_PRIORITY_DEFAULT)

    fun create(name: String, priority: Int): Dispatch {
        val handlerThread = HandlerThread(name, priority)
        handlerThread.start()
        val looper = handlerThread.looper
        return Dispatch(looper)
    }

    val mMainQueue = Dispatch(Looper.getMainLooper())
}