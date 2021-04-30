package cn.net.hylink.flying.core

import android.os.Bundle
import cn.net.hylink.flying.ServiceManager
import cn.net.hylink.flying.constant.Constant

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
    fun dispatch(method: String, requestBundle: Bundle, responseBundle: Bundle) {

        val methodInvoker = requestBundle.getString(Constant.FLY_KEY_ROUTER)?.let {
            ServiceManager.INSTANCE.getCacheMethods(it)
        }
    }
}