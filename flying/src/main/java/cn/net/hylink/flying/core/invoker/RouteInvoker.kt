package cn.net.hylink.flying.core.invoker

import android.os.Bundle
import java.lang.reflect.Method

/**
 * @ClassName RouteInvoker
 * @Description 路由调用
 * @Author haosiyuan
 * @Date 2021/4/21 18:24
 * @Version 1.0
 */
class RouteInvoker constructor(
        private val target: Method,
        private val route: String,
        private val owner: Any
): AbsMethodInvoker(target) {

    /** 参数长度 **/
    private var parametersLength = -1
    /** 参数是否匹配 **/
    private var isMatchParameters = false
    private var matchLength = -1

    init {
        val parameters = target.parameterTypes
        parametersLength = parameters.size
        isMatchParameters = parametersLength == 2

        for (i in 0 until parametersLength) {
            if (!parameters[i].isAssignableFrom(Bundle::class.java)) {
                isMatchParameters = false
                break
            } else {
                matchLength += 1
            }
        }
    }

    override fun invoke(vararg arg: Any?): Any? {
        target.isAccessible = true

        if (isMatchParameters) {
            if (arg.size == 2 && arg[0] is Bundle && arg[1] is Bundle) {
                //2个参数 Bundle
                return invoke(owner, arg)
            } else {
                //如果参数不是 Bundle 则更换为 Bundle
                val parameters = arrayOfNulls<Any>(2)
                if (arg[0] is Bundle) {
                    parameters[0] = arg[0]
                } else {
                    parameters[0] = Bundle()
                }

                if (arg[1] is Bundle) {
                    parameters[1] = arg[1]
                } else {
                    parameters[1] = Bundle()
                }
                return invoke(owner, parameters)
            }
        } else {
            //参数 0、1个  或大于2
            if (parametersLength == 0) return invoke(owner, null)
            val parameters = arrayOfNulls<Any>(parametersLength)
            //获取方法参数
            val types = target.genericParameterTypes
            for (i in 0 until parametersLength) {
                if (i < arg.size && arg[i] is Bundle) {
                    parameters[i] = arg[i]
                } else {
                    //其他类型参数
//                    parameters[i]
                }
            }
            return invoke(owner, parameters)
        }
    }

}