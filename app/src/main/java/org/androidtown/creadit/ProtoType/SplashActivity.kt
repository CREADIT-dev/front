package org.androidtown.creadit.ProtoType

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.androidtown.creadit.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.p1)

        val hd = Handler()
        hd.postDelayed(splashHandler(),1000)
    }
    inner class splashHandler : Runnable{
        override fun run() {
            val intent = Intent(applicationContext, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

    }
}
