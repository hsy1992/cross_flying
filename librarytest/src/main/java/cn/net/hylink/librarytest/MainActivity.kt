package cn.net.hylink.librarytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import cn.net.hylink.flying.ServiceManager
import cn.net.hylink.flying.annotations.FlyingInject
import cn.net.hylink.flying.annotations.Router

class MainActivity : AppCompatActivity() {

    private lateinit var tvMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceManager.INSTANCE.publish(this)
        setContentView(R.layout.activity_main)
        tvMessage = findViewById(R.id.tv_message)
    }

    @Router(path = "/test")
    fun test(requestBundle: Bundle, responseBundle: Bundle) {
        runOnUiThread {
            Log.e("test", Thread.currentThread().name)
            tvMessage.text = requestBundle.getString("test")
        }
    }

    @Router(path = "/testLarge")
    fun testLarge(requestBundle: ByteArray, message: String) {
        runOnUiThread {
            tvMessage.text = message
            Log.i("test", "requestBundle.size:${requestBundle.size}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ServiceManager.INSTANCE.unPublish(this)
    }
}