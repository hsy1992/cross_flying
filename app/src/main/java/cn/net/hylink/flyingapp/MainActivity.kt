package cn.net.hylink.flyingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.net.hylink.flying.core.FlyingMessage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendMessage(view: View) {
//        FlyingMessage(
//            this,
//            "cn.net.hylink.librarytest"
//        )
//            .route("/test").apply {
//                this.mBundle.putString("test", "测试数据")
//            }.fly()

        FlyingMessage(this, "cn.net.hylink.librarytest")
            .route("/testLarge", ByteArray(4096), "message")
            .fly()
    }
}