package cn.net.hylink.flying.core.invoker

import cn.net.hylink.flying.annotations.CrashThrows
import java.util.concurrent.ConcurrentHashMap

/**
 * @ClassName BucketMethod
 * @Description 包装方法
 * @Author haosiyuan
 * @Date 2021/4/20 17:29
 * @Version 1.0
 */
class BucketMethod{

    private lateinit var owner: Any
    private var methods: ConcurrentHashMap<String, IMethodInvoker> = ConcurrentHashMap()

    @CrashThrows
    @Throws(NoSuchMethodException::class)
    fun match(method: String, classes: Array<Class<*>>): IMethodInvoker? {
        val methodUnique = StringBuffer().run {
            this.append(method)
            classes.forEach {
                this.append(it.simpleName)
            }
            this.toString()
        }
        return methods[methodUnique]?.apply {
            val m = owner::class.java.getDeclaredMethod(method, *classes)
            methods[methodUnique] =
                Invoker(m, owner)
        }
    }

}