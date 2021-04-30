package cn.net.hylink.flying.core

import android.content.Context
import android.net.Uri
import android.os.Bundle
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.flybean.FlyArgs
import cn.net.hylink.flying.core.flybean.FlyBundle

/**
 * @ClassName FlyingMessage
 * @Description 调用类
 * @Author haosiyuan
 * @Date 2021/4/26 9:13
 * @Version 1.0
 */
class FlyingMessage constructor(
        private val mContext: Context,
        private val authority: String
) {
    val base = Uri.parse("content://$authority")!!

    val mContextGobal = mContext.applicationContext!!

    companion object {
        val TAG = Constant.PREFIX + FlyingMessage::class.java.simpleName
    }

    fun route(router: String): FlyBundle = FlyBundle(this, router)

    fun route(router: String, vararg params: Any?) = FlyArgs(this, router, params)

    fun fly(requestBundle: Bundle): Bundle? {
        return ConvertFactory.INSTANCE.convertAndFly(this, requestBundle)
    }

    fun fly(router: String, params: Array<Any?>): Bundle? {
        return ConvertFactory.INSTANCE.convertAndFly(this, router, params)
    }
}