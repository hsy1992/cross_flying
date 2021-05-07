package cn.net.hylink.flying.interfaces

/**
 * @ClassName IServiceManager
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/20 16:05
 * @Version 1.0
 */
interface IServiceManager {

    fun publish(service: Any)

    fun unPublish(service: Any)
}