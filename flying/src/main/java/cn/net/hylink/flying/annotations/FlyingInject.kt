package cn.net.hylink.flying.annotations

/**
 * @ClassName Router
 * @Description 注入 支持Activity与Fragment 根据声明周期自动注册、解绑
 * @Author haosiyuan
 * @Date 2021/4/20 14:55
 * @Version 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class FlyingInject