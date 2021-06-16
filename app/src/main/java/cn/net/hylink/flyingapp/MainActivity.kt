package cn.net.hylink.flyingapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.net.hylink.flying.core.FlyingMessage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendMessage(view: View) {
        FlyingMessage(this, "dfsfsdf").route("/sdf").fly()
    }
}