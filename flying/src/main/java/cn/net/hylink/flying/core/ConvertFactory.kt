package cn.net.hylink.flying.core

import android.os.Bundle
import cn.net.hylink.flying.annotations.CrashThrows
import cn.net.hylink.flying.annotations.SingleThread
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.boxing.RouteClientBoxMenImpl
import cn.net.hylink.flying.log.FlyingLog
import java.lang.RuntimeException

/**
 * @ClassName ConvertFactory
 * @Description 转换工厂
 * @Author haosiyuan
 * @Date 2021/4/26 14:12
 * @Version 1.0
 */
class ConvertFactory {

    companion object {
        val INSTANCE =
            SingletonHolder.holder
        val TAG = Constant.PREFIX + ConvertFactory::class.simpleName
    }

    private object SingletonHolder {
        val holder = ConvertFactory()
    }

    @CrashThrows
    @Synchronized
    @Throws(Exception::class)
    fun convertAndFly(flyingMessage: FlyingMessage, requestBundle: Bundle): Bundle?
            = flyingMessage.mContextGobal.contentResolver.call(flyingMessage.base,
                "", null, requestBundle)?.apply {
            parseResponse(requestBundle, this)
    }

    @CrashThrows
    @Synchronized
    @Throws(Exception::class)
    private fun parseResponse(requestBundle: Bundle, responseBundle: Bundle) {
        var message: String? = null
        val code = responseBundle[Constant.FLY_KEY_RESPONSE_CODE]
        when (code) {
            Constant.ResponseCode.FLY_RESPONSE_RESULT_ILLEGAL_ACCESS ->
                message = "illegal access"
            Constant.ResponseCode.FLY_RESPONSE_RESULT_NO_SUCH_METHOD ->
                message = "no such method"
            Constant.ResponseCode.FLY_RESPONSE_RESULT_LOST_CLASS ->
                message = "class not found"
            Constant.ResponseCode.FLY_RESPONSE_RESULT_NOT_FOUND_ROUTE ->
                message = "${requestBundle[Constant.FLY_KEY_ROUTER]} not found"
        }

        message?.let { FlyingLog.e(TAG, "$code, $message") }
        responseBundle.remove(Constant.FLY_KEY_RESPONSE_CODE)
    }

    @CrashThrows
    @Synchronized
    @Throws(Exception::class)
    fun convertAndFly(flyingMessage: FlyingMessage,
                      router: String,
                      params: Array<out Any?>): Bundle? {
        val requestBundle = RouteClientBoxMenImpl()
            .boxing(router, params)
        return RealCall(flyingMessage)
            .execute(router, requestBundle)?.apply {
            parseResponse(requestBundle, this)
        }
    }
}