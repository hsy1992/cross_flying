package cn.net.hylink.flying.queue

import android.os.*
import java.util.concurrent.*

/**
 * @ClassName Dispatch
 * @Description 分发
 * @Author haosiyuan
 * @Date 2021/4/21 9:23
 * @Version 1.0
 */
class Dispatch constructor(private val looper: Looper = Looper.myLooper()!!) : Executor {

    @Volatile
    private var handler = Handler(looper)
    private val ms = 5000L
    private var mMessageQueue: MessageQueue? = null
    private val exchanger = SameThreadExchanger<Any>()
    private val T_OBJECT = Any()
    private val EXCHANGER_THREAD_LOCAL = object : ThreadLocal<Exchanger<Any>>() {
        override fun initialValue(): Exchanger<Any>? {
            return DispatchPairExchanger<Any>()
        }
    }

    companion object {
        private val T_OBJECT = Any()
    }

    /**
     * 发送消息
     */
    fun sendMessage(msg: Message) = sendMessage(msg, 0)

    fun sendMessage(msg: Message, delay: Long) =
            if (delay <= 0) handler.sendMessage(msg)
            else handler.sendMessageDelayed(msg, delay)

    /**
     * 移除runnable
     */
    fun cancelRunnable(runnable: Runnable) = handler.removeCallbacks(runnable)

    /**
     * 线程间交换元素
     */
    fun <T> exchange(callable: Callable<T>): Exchanger<T> {
        try {
            var t: T? = null
            if (Looper.myLooper() == looper) {
                try {
                    t = callable.call()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    exchanger.v = t
                }
                return exchanger as Exchanger<T>
            }
            val exchanger = EXCHANGER_THREAD_LOCAL.get() as DispatchPairExchanger<T>
            handler.post {
                try {
                    t = callable.call()
                    if (ms < 0) {
                        exchanger.exchange0(t)
                    } else {
                        exchanger.exchange0(t, ms, TimeUnit.MILLISECONDS)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return exchanger
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw UnknownError("UnknownError exchange error")
    }

    @Throws(TimeoutException::class)
    fun <T> call(callable: Callable<T>): T? = call(callable, -1L)

    @Throws(TimeoutException::class)
    fun <T> call(callable: Callable<T>, timeout: Long): T? {
        try {
            val exchanger = exchange(callable)
            return if (timeout < 0) {
                exchanger.exchange(T_OBJECT as T)
            } else {
                exchanger.exchange(T_OBJECT as T, timeout, TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    fun postRunnableBlocking(runnable: Runnable) {
        call(Callable<Void> {
            runnable.run()
            null
        })
    }

    /**
     * 分发给 BlockingRunnable
     */
    fun postRunnableScissors(runnable: Runnable) = postRunnableScissors(runnable, -1L)

    fun postRunnableScissors(runnable: Runnable, timeout: Long) {
        if (Looper.myLooper() == looper) {
            runnable.run()
            return
        }
        BlockingRunnable(runnable)
            .postAndWait(handler, timeout)
    }

    /**
     * handler 去执行
     */
    fun postRunnable(runnable: Runnable) = postRunnable(runnable, 0)

    fun postRunnable(runnable: Runnable, delay: Long) =
            if (delay <= 0) handler.post(runnable)
            else handler.postDelayed(runnable, delay)

    /**
     * 立即执行
     */
    fun postRunnableImmediately(runnable: Runnable) {
        if (Looper.myLooper() == looper) {
            runnable.run()
        } else {
            postAtFont(runnable)
        }
    }

    fun postAtFont(runnable: Runnable) = handler.postAtFrontOfQueue(runnable)

    fun post(runnable: Runnable) {
        if (Looper.myLooper() == looper) {
            runnable.run()
        } else {
            postRunnable(runnable)
        }
    }

    fun cleanupQueue() = handler.removeCallbacksAndMessages(null)

    fun addIdleHandler(idleHandler: MessageQueue.IdleHandler): Boolean {
        val messageQueue = getMessageQueue() ?: return false
        messageQueue.addIdleHandler(idleHandler)
        return true
    }

    fun postRunnableInIdleRunning(runnable: Runnable): Boolean {
        val messageQueue = getMessageQueue() ?: return false
        messageQueue.addIdleHandler {
            runnable.run()
            return@addIdleHandler false
        }
        return true
    }

    private fun getMessageQueue(): MessageQueue? {
        if (mMessageQueue != null) {
            return mMessageQueue
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            mMessageQueue = looper.queue
            return mMessageQueue
        }
        try {
            val clazz = looper.javaClass
            val field = clazz.getDeclaredField("mQueue")
            field.isAccessible = true
            val mQueue = field.get(looper)
            if (mQueue is MessageQueue) {
                mMessageQueue = mQueue
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mMessageQueue
    }

    /**
     * 退出
     */
    fun quit() = looper.quit()

    override fun execute(command: Runnable) {
        postRunnable(command)
    }
}