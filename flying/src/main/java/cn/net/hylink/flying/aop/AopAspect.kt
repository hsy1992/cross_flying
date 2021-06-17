package cn.net.hylink.flying.aop

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
    @Pointcut("execution(@cn.net.hylink.flying.annotations.CrashThrows * *(..))")
    fun throwsAnnotated() {
    }

    @Around("throwsAnnotated()")
    fun aroundThrowsJoinPoint(joinPoint: ProceedingJoinPoint): Any? {
        var result: Any? = null
        try {
            result = joinPoint.proceed()
        } catch (e: Exception) {
            FlyingLog.e(message = "flying has error ${e.localizedMessage}")
        }
        return result
    }

}