package cn.net.hylink.flying.queue

import java.lang.RuntimeException
import java.util.concurrent.Exchanger
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * @ClassName DispatchPairExchanger
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/21 10:03
 * @Version 1.0
 */
class DispatchPairExchanger<V>: Exchanger<V>() {

    private var mThreadId = Thread.currentThread().id

    private var mThreadName = Thread.currentThread().name

    @Throws(InterruptedException::class)
    fun exchange0(x: V?): V = super.exchange(x)

    @Throws(InterruptedException::class, TimeoutException::class)
    fun exchange0(x: V?, timeout: Long, unit: TimeUnit?): V = super.exchange(x, timeout, unit)

    @Throws(InterruptedException::class)
    override fun exchange(x: V?): V {
        val id = Thread.currentThread().id
        //判断线程是否相同
        if (id != mThreadId) {
            throw RuntimeException("you must call exchange in the thread id:${id} thread name:${mThreadName}")
        }
        return super.exchange(x)
    }

    @Throws(InterruptedException::class, TimeoutException::class)
    override fun exchange(x: V?, timeout: Long, unit: TimeUnit?): V {
        val id = Thread.currentThread().id
        //判断线程是否相同
        if (id != mThreadId) {
            throw RuntimeException("you must call exchange in the thread id:${id} thread name:${mThreadName}")
        }
        return super.exchange(x, timeout, unit)
    }
}