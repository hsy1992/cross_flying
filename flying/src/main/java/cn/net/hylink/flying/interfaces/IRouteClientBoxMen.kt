package cn.net.hylink.flying.interfaces

/**
 * @ClassName IRouteClientBoxMen
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/26 16:46
 * @Version 1.0
 */
interface IRouteClientBoxMen<T, R>: IBoxMem {

    fun boxing(router: String?, params: Array<Any?>): T?

    fun unboxing(t: T?): R?
}