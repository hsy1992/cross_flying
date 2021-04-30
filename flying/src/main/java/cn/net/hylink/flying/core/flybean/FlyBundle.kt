package cn.net.hylink.flying.core.flybean

import android.os.Bundle
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.FlyingMessage

/**
 * @ClassName FlyBundle
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/26 16:18
 * @Version 1.0
 */
class FlyBundle constructor(
        private val flyingMessage: FlyingMessage,
        private val router: String
) {

    private val mBundle = Bundle()

    /**
     * 发送
     */
    fun fly(): Bundle? {
        return flyingMessage.fly( mBundle.apply {
            this.putString(Constant.FLY_KEY_ROUTER, router)
            this.putInt(Constant.FLY_KEY_FLAGS, Constant.Parameters.FLAGS_BUNDLE)
        })
    }

}