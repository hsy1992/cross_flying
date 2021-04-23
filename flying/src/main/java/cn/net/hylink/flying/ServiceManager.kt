package cn.net.hylink.flying

import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.interfaces.IServiceManager
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * @ClassName ServiceManager
 * @Description 服务管理
 * @Author haosiyuan
 * @Date 2021/4/20 16:39
 * @Version 1.0
 */
class ServiceManager: IServiceManager {


    companion object {
        val instance = SingletonHolder.holder
        val TAG = Constant.PREFIX + ServiceManager::class.simpleName
    }

    private object SingletonHolder {
        val holder = ServiceManager()
    }

    override fun publish(service: Any) {
        TODO("Not yet implemented")
    }

    override fun publish(service: Any, interfaces: KClass<Any>) {
        TODO("Not yet implemented")
    }

    override fun unPublish(service: Any) {
        TODO("Not yet implemented")
    }

    override fun unPublish(service: Any, interfaces: KClass<Any>) {
        TODO("Not yet implemented")
    }
}