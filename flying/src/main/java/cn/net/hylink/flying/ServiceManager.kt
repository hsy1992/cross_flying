package cn.net.hylink.flying

import android.text.TextUtils
import cn.net.hylink.flying.annotations.Router
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.invoker.IMethodInvoker
import cn.net.hylink.flying.core.invoker.RouteInvoker
import cn.net.hylink.flying.interfaces.IServiceManager
import java.util.concurrent.ConcurrentHashMap

/**
 * @ClassName ServiceManager
 * @Description 服务管理
 * @Author haosiyuan
 * @Date 2021/4/20 16:39
 * @Version 1.0
 */
class ServiceManager : IServiceManager {

    companion object {
        val INSTANCE = SingletonHolder.holder
        val TAG = Constant.PREFIX + ServiceManager::class.simpleName
    }

    private val routers: ConcurrentHashMap<String, Array<IMethodInvoker>> = ConcurrentHashMap()

    private object SingletonHolder {
        val holder = ServiceManager()
    }

    @Synchronized
    override fun publish(service: Any) {
        service::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(Router::class.java)?.let {
                if (!TextUtils.isEmpty(it.path)) {
                    method.isAccessible = true
                    cacheMethodToRouter(service::javaClass.name, RouteInvoker(method, it.path, service))
                }
            }
        }
    }

    /**
     * 缓存路由
     */
    @Synchronized
    private fun cacheMethodToRouter(className: String, routeInvoker: RouteInvoker) {
        routers[className]?.let {
            routers[className] = arrayOf(routeInvoker as IMethodInvoker)
        }
    }

    @Synchronized
    override fun unPublish(service: Any) {
        routers.remove(service::javaClass.name)
    }
}