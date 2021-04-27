package cn.net.hylink.flying.interfaces

import android.os.Bundle
import android.util.Pair

/**
 * @ClassName IServerBoxMen
 * @Description 服务端拆箱
 * @Author haosiyuan
 * @Date 2021/4/26 16:49
 * @Version 1.0
 */
interface IServerBoxMen<T> : IBoxMem {

    fun unboxing(t: T?): Pair<Array<Class<*>?>?, Array<Any?>?>?

    fun boxing(requestBundle: Bundle?, responseBundle: Bundle?, value: Any?)
}