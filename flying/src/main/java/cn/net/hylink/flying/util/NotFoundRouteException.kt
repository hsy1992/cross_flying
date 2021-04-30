package cn.net.hylink.flying.util

import java.lang.RuntimeException

/**
 * @ClassName NotFoundRouteException
 * @Description 未找到路由
 * @Author haosiyuan
 * @Date 2021/4/29 16:16
 * @Version 1.0
 */
class NotFoundRouteException(message: String?) : RuntimeException(message)