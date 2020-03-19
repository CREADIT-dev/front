package org.androidtown.creadit.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_join.*
import org.androidtown.creadit.API.API_main
import org.androidtown.creadit.R
import org.androidtown.creadit.SharedPreference.SharedPreferenceBase

class JoinActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferenceBase
    val TAG = "JoinResult"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        init()
    }
    fun init(){

        submitBtn.setOnClickListener { // 여기서 유효성 검사를 해준다.
            val email = userName.text.toString()
            val nick = userId.text.toString()
            val pw = userPw.text.toString()
            val pwCheck = userPwCheck.text.toString()
            val emailChecker = Regex("^[a-zA-Z0-9@.]{10,50}$")
            val nickChecker = Regex("^[0-9a-zA-Z가-힣]{2,20}$") // ^는 시작 $는 문자열의 종료 {}는 범위 []는 집합
            val pwChecker = Regex("^[a-zA-Z0-9!@.#\$%^&*?_~]{8,20}$")

            when {
                email.isEmpty() -> Toast.makeText(this,"email을 입력해주세요",Toast.LENGTH_SHORT).show()
                nick.isEmpty() -> Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()
                pw.isEmpty() -> Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                pwCheck.isEmpty() -> Toast.makeText(this,"비밀번호가 일치하지않습니다",Toast.LENGTH_SHORT).show()
                !emailChecker.matches(email) -> {
                    Toast.makeText(this,"email 양식을 지켜서 입력해주세요",Toast.LENGTH_SHORT).show()
                }
                !nickChecker.matches(nick) -> {
                    Toast.makeText(this,"닉네임은 한글/영문/숫자로 구성된 2~20글자로 입력해주세요",Toast.LENGTH_SHORT).show()
                }
                !pwChecker.matches(pw) -> {
                    Toast.makeText(this,"pw는 영문/숫자로 구성된 8~20글자로 입력해주세요",Toast.LENGTH_SHORT).show()
                }
                !pw.equals(pwCheck) -> {
                    Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this,"체크",Toast.LENGTH_SHORT).show()
                    val disposable = CompositeDisposable()
                    disposable.add(
                        API_main.create(this).join(email, nick, pw)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe ({
                                Log.d(TAG,"result: ${it.code()}")
                                when(it.code()){
                                    200 -> {
                                        Toast.makeText(this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                                        Log.d(TAG,"join success")
                                        val intent = Intent(this,MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else -> {
                                        Toast.makeText(this,"서버 에러",Toast.LENGTH_SHORT).show()
                                        Log.d(TAG,"join fail:${it.code()}")
                                    }
                                }
                            }){
                                // 지금 통신에 성공하게 되면 void 값으로 넘어오기 때문에 error 발생한다.
                                // 이를 위해서 body에 임의의 값을 실어 보내거나 아예 받는 값을 void로 해주면 될 듯 하다.

                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(this,"join exception occurred",Toast.LENGTH_SHORT).show()
                                it.printStackTrace()
                            })
//
//
//                    val rsa = RSACryptor.getInstance()
//                    rsa.init(this)
//
//                    val rsaId = rsa.encrypt(id)
//                    val rsaPw = rsa.encrypt(pw)
//
//                    //서버로 rsa변환 정보 보내야함
//
//
//                    val submitId:HashSet<String>? = null
//                    val submitPw:HashSet<String>? = null
//
//                    submitId!!.add(id)
//                    submitPw!!.add(pw)
//
//                    pref.putSharedPreference("userId",submitId)
//                    pref.putSharedPreference("userPw",submitPw)
                }
            }
            finishActivity(0)
        }

    }
}