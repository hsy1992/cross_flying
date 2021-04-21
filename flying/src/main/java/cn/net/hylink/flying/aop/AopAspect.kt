package cn.net.hylink.flying.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

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
     * 定义切点
     */
    @Pointcut("execution(@cn.net.hylink.flying.annotations.FlyingInject * *(..))")
    fun routerAnnotated() {

    }

    /**
     * 切面处理方法
     */
    @Around("routerAnnotated()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint) {
        //查找到对应的类注入
        print()
    }

}