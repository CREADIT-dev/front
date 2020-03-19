package org.androidtown.creadit.ProtoType

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import org.androidtown.creadit.Activities.JoinActivity
import org.androidtown.creadit.Activities.MainActivity
import org.androidtown.creadit.R
// api 테스트 진행할 액티비티 -> 버튼 추가해서 만들것
class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        init()
    }
    private fun init(){
        pStartBtn.setOnClickListener {
            //            val intent = Intent(this,PmainAcvitity::class.java)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
        pJoinBtn.setOnClickListener{
            val intent = Intent(this,JoinActivity::class.java)
            startActivity(intent)
        }

//        val str = "<font face='돋움체'><b>CREADIT</b></font>"
//        sTextView.append(Html.fromHtml(str))
    }
}