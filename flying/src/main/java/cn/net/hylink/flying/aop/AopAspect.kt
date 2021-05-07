package cn.net.hylink.flying.aop

import cn.net.hylink.flying.ServiceManager
import cn.net.hylink.flying.log.FlyingLog
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * @ClassName AopAspect
 * @Description aop 处理类
 * @Author haosiyuan
 * @Date 2021/4/20 15:27
 * @Version 1.0
 */
@Aspect
class AopAspect {

    /**
     * 定义 Throws
     */
    @Pointcut("execution(@kotlin.jvm.Throws * *(..))")
    fun throwsAnnotated() {

    }

    @Around("throwsAnnotated()")
    fun aroundThrowsJoinPoint(joinPoint: ProceedingJoinPoint) {
        //查找到对应的类注入 判断是否有 onCreate 与 onDestroy
        try {
            joinPoint.proceed()
        } catch (e: Exception) {
            FlyingLog.e(message = "throwsAnnotated has error ${e.localizedMessage}")
        }
    }

}