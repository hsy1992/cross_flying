package cn.net.hylink.flying.core.invoker

import java.lang.reflect.Method

/**
 * @ClassName AbsMethodInvoker
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/20 17:37
 * @Version 1.0
 */
class AbsMethodInvoker (
     private val target: Method
): IMethodInvoker {

    private var mDispatch

    init {

    }

    override fun invoke(vararg arg: Any?): Any? {
        TODO("Not yet implemented")
    }
}