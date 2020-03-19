package org.androidtown.creadit.ProtoType

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.androidtown.creadit.R

class registerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }
    private fun init(){
        val str = intent.extras?.getString("str")
        val num = intent.extras?.getInt("num")
        val tagBtn = findViewById<TextView>(R.id.tagBtn)
        registerTextView.text = str

        when(num){
            1-> tagBtn.text = "개인 채널 관리"
            2-> tagBtn.text = "저작권"
            3-> tagBtn.text = "기타"
            4-> tagBtn.text = "편집"
            5-> tagBtn.text = "장비 및 공간"
            6-> tagBtn.text = "전문 채널"
            else-> tagBtn.text = "없음"
        }
    }
}
