package cn.net.hylink.flying.core.invoker

import java.lang.reflect.InvocationTargetException

/**
 * @ClassName MethodInvoker
 * @Description 调用方法得接口
 * @Author haosiyuan
 * @Date 2021/4/20 17:29
 * @Version 1.0
 */
interface IMethodInvoker {

    /**
     * 调用方法
     * vararg 可变参数
     */
    @Throws(
        IllegalAccessException::class,
        InvocationTargetException::class
    )
    operator fun invoke(vararg arg: Any?): Any?

}