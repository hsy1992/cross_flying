package cn.net.hylink.flying.core.invoker

import cn.net.hylink.flying.annotations.MainThread
import cn.net.hylink.flying.annotations.SingleThread
import cn.net.hylink.flying.queue.Dispatch
import cn.net.hylink.flying.queue.DispatchThread
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.concurrent.Callable

/**
 * @ClassName AbsMethodInvoker
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/20 17:37
 * @Version 1.0
 */
abstract class AbsMethodInvoker (
     private val target: Method
): IMethodInvoker {

    private var mDispatch: Dispatch? = when {
        target.getAnnotation(MainThread::class.java) != null -> {
            DispatchThread.MAIN_THREAD
        }
        target.getAnnotation(SingleThread::class.java) != null -> {
            DispatchThread.SINGLE_THREAD
        }
        else -> null
    }

    /**
     * 不分发直接调用
     */
    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun invoke(owner: Any, vararg arg: Any?): Any? =
            if (mDispatch == null) target.invoke(owner, arg) else invokeByDispatch(owner, arg)

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    private fun invokeByDispatch(owner: Any, vararg arg: Any?): Any? {
        val exceptions = arrayOfNulls<Exception>(1)
        val o: Any? = mDispatch?.call(Callable {
            var returnObject: Any? = null
            try {
                returnObject = target.invoke(owner, *arg)
            } catch (e: Exception) {
                e.printStackTrace()
                exceptions[0] = e
            }
            returnObject
        })

        if (exceptions[0] != null) {
            when {
                exceptions[0] is InvocationTargetException -> {
                    throw (exceptions[0] as InvocationTargetException?)!!
                }
                exceptions[0] is IllegalAccessException -> {
                    throw (exceptions[0] as IllegalAccessException?)!!
                }
                else -> {
                    throw (exceptions[0] as RuntimeException?)!!
                }
            }
        }
        return o
    }

}