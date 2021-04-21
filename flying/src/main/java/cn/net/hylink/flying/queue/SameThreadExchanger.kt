package cn.net.hylink.flying.queue

import java.util.concurrent.Exchanger
import java.util.concurrent.TimeUnit

/**
 * @ClassName ThreadExchanger
 * @Description 线程交换
 * @Author haosiyuan
 * @Date 2021/4/21 9:59
 * @Version 1.0
 */
class SameThreadExchanger<V>: Exchanger<V>() {

    var v: V? = null

    override fun exchange(x: V, timeout: Long, unit: TimeUnit?): V? {
        return exchange(x)
    }

    override fun exchange(x: V): V? {
        try {
            return v
        } finally {
            v = null
        }
    }

}