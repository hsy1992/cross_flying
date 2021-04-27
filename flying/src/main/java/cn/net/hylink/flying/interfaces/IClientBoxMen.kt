package cn.net.hylink.flying.interfaces

import java.lang.reflect.Type

/**
 * @ClassName IClientBoxMen
 * @Description 连接
 * @Author haosiyuan
 * @Date 2021/4/26 16:44
 * @Version 1.0
 */
interface IClientBoxMen<T, P, R> : IBoxMem {
    fun boxing(args: Array<Any?>?, types: Array<Type?>?, returnType: Type?): T

    fun unboxing(p: P): R
}