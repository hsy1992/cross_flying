package cn.net.hylink.flying.annotations

/**
 * @ClassName Router
 * @Description 路由地址的路径
 * @Author haosiyuan
 * @Date 2021/4/20 14:55
 * @Version 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Router (
    /**
     *  路由地址
     */
    val path: String
)