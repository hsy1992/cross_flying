package cn.net.hylink.flying.aop

import android.os.Bundle
import cn.net.hylink.flying.ServiceManager
import cn.net.hylink.flying.annotations.Router
import cn.net.hylink.flying.log.FlyingLog
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

    private val onCreate = "onCreate"

    private val onDestroy = "onDestroy"

    /**
     * 定义切点 onCreate
     */
    @Pointcut("execution(* *.onCreate(..)) && execution(@cn.net.hylink.flying.annotations.FlyingInject * *(..))")
    fun onCreateAnnotated() {

    }

    /**
     * 定义切点 onDestroy
     */
    @Pointcut("execution(* *.onDestroy(..)) && execution(@cn.net.hylink.flying.annotations.FlyingInject * *(..))")
    fun onDestroyAnnotated() {

    }

    /**
     * 定义 Throws
     */
    @Pointcut("execution(@kotlin.jvm.Throws * *(..))")
    fun throwsAnnotated() {

    }

    /**
     * 切面处理方法
     */
    @Around("onCreateAnnotated()")
    fun aroundOnCreateJoinPoint(joinPoint: ProceedingJoinPoint) {
        //查找到对应的类注入 判断是否有 onCreate 与 onDestroy
        try {
            ServiceManager.INSTANCE.publish(joinPoint.`this`!!)
            joinPoint.proceed()
        } catch (e: Exception) {
            e.printStackTrace()
            FlyingLog.e(message = "onCreateAnnotated has error ${e.localizedMessage}")
        }
    }

    @Around("onDestroyAnnotated()")
    fun aroundOnDestroyJoinPoint(joinPoint: ProceedingJoinPoint) {
        //查找到对应的类注入 判断是否有 onCreate 与 onDestroy
        try {
            ServiceManager.INSTANCE.unPublish(joinPoint.`this`!!)
            joinPoint.proceed()
        } catch (e: Exception) {
            e.printStackTrace()
            FlyingLog.e(message = "onDestroyAnnotated has error ${e.localizedMessage}")
        }
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