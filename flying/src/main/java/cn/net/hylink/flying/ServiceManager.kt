package cn.net.hylink.flying

import cn.net.hylink.flying.annotations.Router
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.invoker.IMethodInvoker
import cn.net.hylink.flying.core.invoker.RouteInvoker
import cn.net.hylink.flying.interfaces.IServiceManager
import cn.net.hylink.flying.log.FlyingLog
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

    private val routers: ConcurrentHashMap<String, ArrayList<IMethodInvoker>> = ConcurrentHashMap()
    private val services: ConcurrentHashMap<String, IMethodInvoker> = ConcurrentHashMap()

    private object SingletonHolder {
        val holder = ServiceManager()
    }

    @Synchronized
    override fun publish(service: Any) {
        service::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(Router::class.java)?.let {
                if (it.path.isNotEmpty()) {
                    method.isAccessible = true
                    FlyingLog.i(message = service::class.java.name)
                    cacheMethodToRouter(service::class.java.name,
                        RouteInvoker(
                            it.path,
                            method,
                            service
                        )
                    )
                }
            }
        }
    }

    /**
     * 缓存路由
     */
    @Synchronized
    private fun cacheMethodToRouter(className: String, routeInvoker: RouteInvoker) {
        FlyingLog.d(TAG, "缓存路由：$className,路由地址:${routeInvoker.path},${routeInvoker.target.name}")

        services[routeInvoker.path] = routeInvoker
        val array = arrayListOf<IMethodInvoker>(routeInvoker)
        routers[className]?.let {
            array.addAll(it)
        }
        routers[className] = array
    }

    @Synchronized
    override fun unPublish(service: Any) {
        routers.remove(service::class.java.name)
        service::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(Router::class.java)?.let {
                if (it.path.isNotEmpty()) {
                    services.remove(it.path)
                    FlyingLog.d(TAG, "解绑路由：${service::class.java.name}," +
                            "路由地址:${it.path},${method.name}")
                }
            }
        }
    }

    /**
     * 获取缓存得方法
     */
    fun getCacheMethods(router: String): IMethodInvoker? = services[router]
}