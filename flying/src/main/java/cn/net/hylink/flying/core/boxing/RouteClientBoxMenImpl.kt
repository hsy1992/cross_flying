package cn.net.hylink.flying.core.boxing

import android.os.Bundle
import cn.net.hylink.flying.constant.Constant
import cn.net.hylink.flying.interfaces.IRouteClientBoxMen
import cn.net.hylink.flying.util.Utils
import java.util.*

/**
 * @ClassName RouteClientBoxMenImpl
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/4/26 17:02
 * @Version 1.0
 */
class RouteClientBoxMenImpl:
    IRouteClientBoxMen<Bundle, Bundle> {

    override fun boxing(router: String?, params: Array<out Any?>): Bundle {
        return Bundle().apply {
            this.putInt(Constant.FLY_KEY_LENGTH, params.size)
            this.putString(Constant.FLY_KEY_ROUTER, router)
            this.putInt(Constant.FLY_KEY_FLAGS, Constant.Parameters.FLAGS_ARGS)
            setValues(this, params)
        }
    }

    private fun setValues(bundle: Bundle, params: Array<out Any?>) {
        params.forEachIndexed { i, any ->
            val index = String.format(Locale.ENGLISH, Constant.FLY_KEY_INDEX, i)
            if (any == null) {
                val indexClass = String.format(Locale.ENGLISH, Constant.FLY_KEY_CLASS_INDEX, i)
                bundle.putString(index, "")
                bundle.putString(indexClass, "")
            } else {
                Utils.convert(index, bundle, any)
            }
        }
    }

    override fun unboxing(t: Bundle?): Bundle? = t
}