package cn.net.hylink.flying.core.invoker

import java.lang.reflect.Method

/**
 * @ClassName Invoker
 * @Description Invoker 调用方法
 * @Author haosiyuan
 * @Date 2021/4/21 18:17
 * @Version 1.0
 */
class Invoker constructor(
        private val target: Method,
        private val owner: Any
) : AbsMethodInvoker(target) {

    override fun invoke(vararg arg: Any?): Any? {
        target.isAccessible = true
        return invoke(owner, arg)
    }
}