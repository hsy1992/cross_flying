package cn.net.hylink.flying.core.flybean

import cn.net.hylink.flying.core.FlyingMessage

/**
 * @ClassName FlyArgs
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/26 16:27
 * @Version 1.0
 */
class FlyArgs constructor(
        private val flyingMessage: FlyingMessage,
        private val router: String,
        vararg args: Any?
) {
    private val params = args

    fun fly(largeData: Boolean = false) {
        flyingMessage.fly(router, largeData, params)
    }
}