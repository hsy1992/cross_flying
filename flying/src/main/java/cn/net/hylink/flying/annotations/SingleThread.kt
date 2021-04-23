package cn.net.hylink.flying.annotations

/**
 * @ClassName SingleThread
 * @Description 线程去处理
 * @Author haosiyuan
 * @Date 2021/4/21 17:26
 * @Version 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SingleThread (
)