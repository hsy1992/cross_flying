package cn.net.hylink.flying.log

import android.util.Log
import cn.net.hylink.flying.BuildConfig

/**
 * @ClassName FlyingLog
 * @Description logo
 * @Author haosiyuan
 * @Date 2021/4/20 11:57
 * @Version 1.0
 */
class FlyingLog {

    companion object {
        val DEBUG = BuildConfig.DEBUG

        private const val DEFAULT_TAG = "DEFAULT_TAG"

        fun i(tag: String = DEFAULT_TAG, message: String) {
            Log.i(tag, message)
        }

        fun e(tag: String = DEFAULT_TAG, message: String) {
            Log.e(tag, message)
        }

        fun d(tag: String = DEFAULT_TAG, message: String) {
            Log.d(tag, message)
        }
    }

}