package cn.net.hylink.flying.core

import android.os.Bundle
import cn.net.hylink.flying.ServiceManager
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.core.boxing.ServerBoxMenImpl
import cn.net.hylink.flying.core.invoker.RouteInvoker
import cn.net.hylink.flying.log.FlyingLog
import cn.net.hylink.flying.util.NotFoundRouteException

/**
 * @ClassName Server
 * @Description 处理服务端
 * @Author haosiyuan
 * @Date 2021/4/29 16:10
 * @Version 1.0
 */
class Server {

    companion object {
        val INSTANCE = SingletonHolder.holder
        val TAG = Constant.PREFIX + Server::class.simpleName
    }

    private object SingletonHolder {
        val holder = Server()
    }

    /**
     * 分发消息
     */
    @Synchronized
    fun dispatch(method: String, requestBundle: Bundle, responseBundle: Bundle) {

        val router = requestBundle.getString(Constant.FLY_KEY_ROUTER)
        val callers = router?.let {
            ServiceManager.INSTANCE.getCacheMethods(it)
        } ?: throw NotFoundRouteException(
            "$router was not found"
        )

        //传送标识
        when (requestBundle.getInt(Constant.FLY_KEY_FLAGS)) {
            Constant.Parameters.FLAGS_BUNDLE -> {
                //分发方法到 bundle,bundle
                callers.invoke(requestBundle, responseBundle)
                responseBundle.putInt(
                    Constant.FLY_KEY_RESPONSE_CODE,
                    Constant.ResponseCode.FLY_RESPONSE_RESULT_SUCCESS
                )
                FlyingLog.d(TAG, "response FLAGS_BUNDLE call back router $router")
            }
            Constant.Parameters.FLAGS_ARGS -> {
                val args = ServerBoxMenImpl().unboxing(requestBundle)
                callers.invoke(*args)
                responseBundle.putInt(
                    Constant.FLY_KEY_RESPONSE_CODE,
                    Constant.ResponseCode.FLY_RESPONSE_RESULT_SUCCESS
                )
                FlyingLog.d(TAG, "response FLAGS_ARGS call back router $router")
            }
        }


    }
}