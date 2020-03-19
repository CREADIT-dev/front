package org.androidtown.creadit.API

import android.content.Context
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.androidtown.creadit.Data.Join_res
import org.androidtown.creadit.Data.Login_res
import org.androidtown.creadit.Data.SearchList_res
import org.androidtown.creadit.SharedPreference.AddCookiesInterceptor
import org.androidtown.creadit.SharedPreference.ReceivedCookiesInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface API_main { // retrofit을 class로 만들어서 반환한다.
    companion object{
        private fun provideHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            receivedCookiesInterceptor: ReceivedCookiesInterceptor,
            addCookiesInterceptor: AddCookiesInterceptor
        ):OkHttpClient{
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.addInterceptor(loggingInterceptor)
            okHttpClient.addInterceptor(receivedCookiesInterceptor)
            okHttpClient.addInterceptor(addCookiesInterceptor)

            return okHttpClient.build()
        }

        private fun provideHttpLoggingInterceptor():HttpLoggingInterceptor{
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

        fun create(context: Context):API_main{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://13.209.242.186:80")
                .client(
                    provideHttpClient(
                        provideHttpLoggingInterceptor(),
                        ReceivedCookiesInterceptor(context),
                        AddCookiesInterceptor(context)
                    )
                )
                .build()

            return retrofit.create(API_main::class.java)
        }
    }

    @FormUrlEncoded
    @POST("auth/sign-up")
    fun join(
        @Field("email") email:String,
        @Field("display_name") nickname: String,
        @Field("password") pw: String
    ): Observable<Response<Join_res>>

    @FormUrlEncoded
    @POST("auth/login") //로그인
    fun login(
        @Field("username") id: String,
        @Field("password") pw: String
    ): Observable<Response<Login_res>>

    @GET("/test") // test
    fun test(

    ):Observable<SearchList_res>
}