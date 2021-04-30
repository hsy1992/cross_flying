package cn.net.hylink.flying.core.invoker

import cn.net.hylink.flying.util.Utils
import java.lang.reflect.Method

/**
 * @ClassName RouteInvoker
 * @Description 路由调用 Bundle 等基础类型  参数Bundle in, Bundle out out为返回回调
 * @Author haosiyuan
 * @Date 2021/4/21 18:24
 * @Version 1.0
 */
class RouteInvoker constructor(
        private val target: Method,
        val route: String,
        private val owner: Any
) : AbsMethodInvoker(target) {

    private var types = target.parameterTypes

    /** 参数长度 **/
    private var parametersLength = types.size

    init {
        for (i in 0 until parametersLength) {
            if (!Utils.checkClassAccord(types[i])) throw RuntimeException("returnType not support")
        }
    }

    override fun invoke(vararg arg: Any?): Any? {
        target.isAccessible = true

        if (parametersLength == 0) return invoke(owner, null)
        val parameters = arrayOfNulls<Any>(parametersLength)

        //获取方法参数
        for (i in 0 until parametersLength) {
            arg[i]?.let {
                if (it::class.java != types[i]) {
                    throw RuntimeException("invoke fail with bad arg")
                }
                parameters[i] = arg[i]
            }
        }
        return invoke(owner, parameters)
    }

}