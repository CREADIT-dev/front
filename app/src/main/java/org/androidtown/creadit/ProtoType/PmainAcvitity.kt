package org.androidtown.creadit.ProtoType

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pmain_acvitity.*
import org.androidtown.creadit.R

class PmainAcvitity : AppCompatActivity() {
    private lateinit var title:LinearLayout
    private lateinit var editText : EditText
    private lateinit var linearLayout: LinearLayout
    private lateinit var sendBtn:TextView
    private var toggle:Boolean = true
    private var check:Boolean = false
    private var num:Int = 0
    private var t1:Boolean = false
    private var t2:Boolean = false
    private var t3:Boolean = false
    private var t4:Boolean = false
    private var t5:Boolean = false
    private var t6:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pmain_acvitity)
        init()
    }
    private fun init(){
        val translate = AnimationUtils.loadAnimation(this,R.anim.translate_top)
        translate.setAnimationListener(SlidingListener())
        linearLayout = findViewById(R.id.linearLayout)
        editText = findViewById(R.id.editTextView)
        title = findViewById(R.id.slidingLayout)
        sendBtn = findViewById(R.id.sendBtn)
        title.setOnClickListener {
            if(toggle) title.startAnimation(translate)
        }
        sendBtn.setOnClickListener {
            if(check){
                if(toggle){
                    layout3.visibility = View.VISIBLE
                    layout1.visibility = View.GONE
                    layout2.visibility = View.VISIBLE
                    title.visibility = View.GONE
                    titleTextView.visibility = View.GONE
                    sendBtn.setBackgroundResource(R.drawable.circlebtn2_layout)
                    sendBtn.setTextColor(resources.getColor(R.color.orange))
                    sendBtn.text = "확인"
                }else{
                    val intent = Intent(this,registerActivity::class.java)
                    intent.putExtra("num",num)
                    intent.putExtra("str",editTextView.text.toString())
                    startActivity(intent)
//                    layout3.visibility = View.GONE
//                    layout1.visibility = View.VISIBLE
//                    layout2.visibility = View.VISIBLE
//                    title.visibility = View.VISIBLE
//                    titleTextView.visibility = View.VISIBLE
//                    sendBtn.setBackgroundResource(R.drawable.circlebtn_layout)
//                    sendBtn.setTextColor(resources.getColor(R.color.colorSplashBackground))
//                    sendBtn.text = "SEND"
                }
                toggle = !toggle
            }
        }
        b1.setOnClickListener {
            if(!t1){
                b1.setBackgroundResource(R.drawable.circlebtn_layout)
                b1.setTextColor(resources.getColor(R.color.colorSplashBackground))
                b1.elevation = -10.0F
                num = 1
            }else{
                b1.setBackgroundResource(R.drawable.circlebtn2_layout)
                b1.setTextColor(resources.getColor(R.color.orange))
                b1.elevation = 10.0F
            }
            t1 = !t1
        }
        b2.setOnClickListener {
            if(!t2){
                b2.setBackgroundResource(R.drawable.circlebtn_layout)
                b2.setTextColor(resources.getColor(R.color.colorSplashBackground))
                b2.elevation = -10.0F
                num = 2
            }else{
                b2.setBackgroundResource(R.drawable.circlebtn2_layout)
                b2.setTextColor(resources.getColor(R.color.orange))
                b2.elevation = 10.0F
            }
            t2 = !t2
        }
        b3.setOnClickListener {
            if(!t3){
                b3.setBackgroundResource(R.drawable.circlebtn_layout)
                b3.setTextColor(resources.getColor(R.color.colorSplashBackground))
                b3.elevation = -10.0F
                num = 3
            }else{
                b3.setBackgroundResource(R.drawable.circlebtn2_layout)
                b3.setTextColor(resources.getColor(R.color.orange))
                b3.elevation = 10.0F
            }
            t3 = !t3
        }
        b4.setOnClickListener {
            if(!t4){
                b4.setBackgroundResource(R.drawable.circlebtn_layout)
                b4.setTextColor(resources.getColor(R.color.colorSplashBackground))
                b4.elevation = -10.0F
                num = 4
            }else{
                b4.setBackgroundResource(R.drawable.circlebtn2_layout)
                b4.setTextColor(resources.getColor(R.color.orange))
                b4.elevation = 10.0F
            }
            t4 = !t4
        }
        b5.setOnClickListener {
            if(!t5){
                b5.setBackgroundResource(R.drawable.circlebtn_layout)
                b5.setTextColor(resources.getColor(R.color.colorSplashBackground))
                b5.elevation = -10.0F
                num = 5
            }else{
                b5.setBackgroundResource(R.drawable.circlebtn2_layout)
                b5.setTextColor(resources.getColor(R.color.orange))
                b5.elevation = 10.0F
            }
            t5 = !t5
        }
        b6.setOnClickListener {
            if(!t6){
                b6.setBackgroundResource(R.drawable.circlebtn_layout)
                b6.setTextColor(resources.getColor(R.color.colorSplashBackground))
                b6.elevation = -10.0F
                num = 6
            }else{
                b6.setBackgroundResource(R.drawable.circlebtn2_layout)
                b6.setTextColor(resources.getColor(R.color.orange))
                b6.elevation = 10.0F
            }
            t6 = !t6
        }
    }

    inner class SlidingListener : Animation.AnimationListener{
        override fun onAnimationRepeat(animation: Animation?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onAnimationEnd(animation: Animation?) {
            Log.d("pmain","end")
            titleTextView.setTextColor(resources.getColor(R.color.colorSplashBackground))
            title.setOnClickListener(null)
            editText.visibility = View.VISIBLE
            linearLayout.visibility = View.VISIBLE
            check = !check
        }

        override fun onAnimationStart(animation: Animation?) {
            Log.d("pmain","start")
        }
    }
}
