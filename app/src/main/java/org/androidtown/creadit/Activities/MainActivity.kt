package org.androidtown.creadit.Activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.androidtown.creadit.API.API_main
import org.androidtown.creadit.ProtoType.PmainAcvitity
import org.androidtown.creadit.RSA.RSACryptor



class MainActivity : AppCompatActivity(){
    private lateinit var permissionListener : PermissionListener
    private lateinit var rsa : RSACryptor
    private lateinit var textView : TextView
    private lateinit var searchText: AutoCompleteTextView
    private var arrayList = ArrayList<String>()
    private var mLastInputTime : Long = 0
    private lateinit var mAdapter:ArrayAdapter<String>
//    private lateinit var komoran : Komoran
    //LIGHT 모델(DEFAULT_MODEL.LIGHT)은 일반적으로 사용되는 문장들을 학습한 모델로 다양한 분야에서 사용하실 수 있는 기본 모델입니다.
    //FULL 모델(DEFAULT_MODEL.FULL)은 LIGHT 모델에 위키피디아의 타이틀을 NNP(고유명사)로 포함해서 학습한 것이며 그러므로 LIGHT 모델보다 상대적으로 용량이 큽니다.

    private val TAG = "LoginResult"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.androidtown.creadit.R.layout.activity_main)
        init()
    }
//    private fun setTestList(){
//        Log.d("searchText","textList")
//        mAdapter.clear()
//        mAdapter.add("유튜브 편집자 구인구직")
//        mAdapter.add("안녕하세요")
//        mAdapter.add("저는 김대현이라고 합니다.")
//        mAdapter.add("CREADIT 개발을 맡고있습니다.")
//        mAdapter.add("시험 동작중입니다.")
//        mAdapter.add("하하하")
//        mAdapter.notifyDataSetChanged()
//    }
//    private fun setTestList2(){
//        Log.d("searchText","textList2")
//        mAdapter.clear()
//        mAdapter.add("변경된 list")
//        mAdapter.add("정상 변경됨")
//        mAdapter.add("dummy1")
//        mAdapter.add("dummy2")
//        mAdapter.add("dummy3")
//        mAdapter.add("dummy4")
//        mAdapter.add("dummy5")
//        mAdapter.notifyDataSetChanged()
//    }

    private fun init(){ // 각종 값들 초기화
        permissionCheck()

        rsa = RSACryptor.getInstance()
        rsa.init(this)
//        komoran = Komoran(DEFAULT_MODEL.FULL)
        textView = findViewById(org.androidtown.creadit.R.id.strResult)
        textView.movementMethod = ScrollingMovementMethod()

//        setTestList()
        searchText = findViewById(org.androidtown.creadit.R.id.searchText)
        mAdapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, arrayList)
        searchText.setAdapter(mAdapter)
        searchText.addTextChangedListener(object:TextWatcher{ // 검색을 위한 장치. 타이핑을 멈춘뒤 2초가 지나면 서버로 list request 보낸다.
            override fun afterTextChanged(s: Editable?) {
//                if (SystemClock.elapsedRealtime() - mLastInputTime > 2000) {
//                    Log.d("searchText","동작1")
//                    arrayList.clear()
//                    if((mLastInputTime%2).toInt() == 0){
//                        Log.d("searchText","동작2")
//                        setTestList2()
//                    }else{
//                        Log.d("searchText","동작3")
//                        setTestList()
//                    }
////                    CompositeDisposable(API_main.create(this@MainActivity).test()
////                        .observeOn(AndroidSchedulers.mainThread())
////                        .subscribeOn(Schedulers.io())
////                        .subscribe({
////                            arrayList.clear()
////                            arrayList = it.arrayList
////                        }){
////                            Log.d(TAG,"searchList error")
////                        })
//                }
//                Log.d("searchText","동작x")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (SystemClock.elapsedRealtime() - mLastInputTime > 1000) {
                    Log.d("searchText","일정시간 경과시 request")

                    CompositeDisposable(
                        API_main.create(this@MainActivity).test()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                mAdapter.clear()
                                mAdapter.addAll(it.arrayList)
                                Log.d(TAG,"autocomplete success")
                            }){
                                Log.d(TAG,"autocomplete fail")
                            }
                    )
                }else{
                    Log.d("searchText","동작x")
                }
                mLastInputTime = SystemClock.elapsedRealtime() // 마지막 입력시간 측정
            }
        })

        joinBtn.setOnClickListener { // 회원가입
            val intent = Intent(this,JoinActivity::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener {
            val id = loginId.text.toString()
            val pw = loginPw.text.toString()
            CompositeDisposable().add(
                API_main.create(this).login(id,pw)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.d(TAG,it.body()!!.token)
                        when(it.code()){
                            200->{
                                Log.d(TAG,"로그인 성공")
                                val intent = Intent(this,PmainAcvitity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else->{
                                Log.d(TAG,"로그인 실패")
                            }
                        }
                    }){
                        it.printStackTrace()

                    }
            )
//            val id = loginId.text.toString()
//            val pw = loginPw.text.toString()
//
//            val rsaId = rsa.encrypt(id)
//            val rsaPw = rsa.encrypt(pw)
//
//            val idChecker = Regex("^[a-zA-Z0-9]*$")
//            val pwChecker = Regex("^[a-zA-Z0-9!@.#\$%^&*?_~]{8,20}$")
//
//            when{
//                !idChecker.matches(id) || id.isEmpty() -> Toast.makeText(this,"id를 정확하게 입력해주세요",Toast.LENGTH_SHORT).show()
//                !pwChecker.matches(pw) || pw.isEmpty() -> Toast.makeText(this,"pw를 정확하게 입력해주세요",Toast.LENGTH_SHORT).show()
//                else -> {
//                    val disposable = CompositeDisposable()
//                    disposable.add(API_main.create(this)
//                        .login(id,pw)
////                        .test()
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
////                        .subscribe({
////                            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show()
////                            Log.d(TAG,"success")
////                        }){
////                            Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show()
////                            Log.d(TAG,"fail")
////                        })
//                        .subscribe({
//                            when(it.loginRes){
//                                "200" -> {
//                                    // 로그인 후 화면 전환 / session 저장해야함
//                                    Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
//                                    Log.d(TAG,"login success")
//                                }
//                                else -> {
//                                    Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
//                                    Log.d(TAG,"login fail")
//                                }
//                            }
//                        }){
//                            Toast.makeText(this,"Login exception occured",Toast.LENGTH_SHORT).show()
//                            Log.d(TAG,"Login exception occured")
//                            it.printStackTrace()
//                        })
//                }
//            }
        }
        searchBtn.setOnClickListener {
            textView.text = ""

        }
    }

//    private fun excuteKomoran(){ // 형태소 분석 실행
//
//        val str = searchText.text.toString()
//
//        if(!str.isEmpty()){
//            val analyzeResult = komoran.analyze(str)
//            val tokenList = analyzeResult.tokenList
//            for(token in tokenList){
//                val result = "token: (${token.beginIndex} , ${token.endIndex}) , ${token.morph} / ${token.pos}\n"
//                textView.append(result)
//            }
//        }
////        Thread (object :Runnable{
////            override fun run() {
////                val str = searchText.text.toString()
////                val analyzeResult = komoran.analyze(str)
////                val tokenList = analyzeResult.tokenList
////
////
////                runOnUiThread(object:Runnable{
////                    override fun run() {
////                        for(token in tokenList){
////                            val result = "token: (${token.beginIndex} , ${token.endIndex}) , ${token.morph} / ${token.pos}\n"
////                            textView.append(result)
////                        }
////                        Log.d("thread1","작동중")
////                    }
////                })
////            }
////        }).start()
//    }

    private fun permissionCheck(){ // 사용자 권한 체크
        permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(applicationContext,"권한 승인 됨", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext,"권한 승인거부 됨", Toast.LENGTH_SHORT).show()
            }
        }
        //여기 다시
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage("CREADIT을 사용하기 위해서는 접근권한이 필요해요")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.INTERNET,android.Manifest.permission.CAMERA)
            .check()
    }
}