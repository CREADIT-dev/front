package org.androidtown.creadit.RSA

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.*
import java.security.cert.CertificateException
import java.security.spec.RSAKeyGenParameterSpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.security.auth.x500.X500Principal

class RSACryptor {
    private lateinit var keyEntry:KeyStore.Entry
    companion object{
        private const val TAG :String = "RSACryptor"
        private const val CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
        private lateinit var INSTANCE:RSACryptor
        fun getInstance():RSACryptor{
            INSTANCE = RSACryptor()
            return INSTANCE
        }
    }

    //Android Keystore 시스템에서는 암호화 키를
    //컨테이너(시스템만이 접근 가능한 곳)에 저장해야 하므로
    //이 키를 기기에서 추출해내기가 더 어려움

    fun init(context: Context){
        try{
            //AndroidKeyStore 기입
            var ks:KeyStore = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            //KeyStore에 해당 패키지 네임이 등록되어있는가?
            if(!ks.containsAlias(context.packageName)){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    initAndroidM(context.packageName)
                }else{
                    initAndroidK(context)
                }
            }
            keyEntry = ks.getEntry(context.packageName,null)
        }catch (e:KeyStoreException){
            Log.e(TAG,"Initialize fail",e)
        }catch (e:IOException){
            Log.e(TAG,"Initialize fail",e)
        }catch (e:NoSuchAlgorithmException){
            Log.e(TAG,"Initialize fail",e)
        }catch (e:CertificateException){
            Log.e(TAG,"Initialize fail",e)
        }catch (e:UnrecoverableEntryException){
            Log.e(TAG,"Initialize fail",e)
        }
    }

    // API Level 23 이상(마쉬멜로우) 개인키 생성
    private fun initAndroidM(alias:String){
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                var kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA,"AndroidKeyStore")

                kpg.initialize(
                    KeyGenParameterSpec.Builder(
                        alias,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    ).setAlgorithmParameterSpec(
                        RSAKeyGenParameterSpec(
                            2048,
                            RSAKeyGenParameterSpec.F4
                        )
                    ).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1 //이부분 중요
                    ).setDigests(
                        KeyProperties.DIGEST_SHA512,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA256
                    ).setUserAuthenticationRequired(false).build()
                )

                kpg.generateKeyPair()
                Log.d(TAG,"RSA Initialize")
            }
        }catch (e:GeneralSecurityException){
            Log.e(TAG,"이 디바이스는 관련 알고리즘을 제공하지 않음.",e)
        }
    }

    private fun initAndroidK(context:Context){
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                //유효성 기간
                var start: Calendar = Calendar.getInstance()
                var end:Calendar = Calendar.getInstance()
                end.add(Calendar.YEAR,25)

                //AndroidKeyStore 정확하게 기입
                var kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA","AndroidKeyStore")

                kpg.initialize(KeyPairGeneratorSpec.Builder(context)
                    .setKeySize(2048)
                    .setAlias(context.packageName)
                    .setSubject(X500Principal("CN=myKey"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
                )
                kpg.genKeyPair()

                Log.d(TAG,"RSA Initailize")
            }
        }catch (e: GeneralSecurityException){
            Log.d(TAG,"이 디바이스는 관련 알고리즘을 지원하지 않음.")
        }
    }

    //문자열 위주로 작업하기 때문에 반드시 String형이나 toString을 쓸 것!!
    //암호화(set)
    fun encrypt(plain:String):String{
        try{
            var bytes = plain.toByteArray(charset("UTF-8"))
            var cipher:Cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            //public key로 암호화
            cipher.init(Cipher.ENCRYPT_MODE, (keyEntry as KeyStore.PrivateKeyEntry).certificate.publicKey)
            val encryptedBytes = cipher.doFinal(bytes)

            Log.d(TAG, "Encrypted Text : " + String(Base64.encode(encryptedBytes,Base64.DEFAULT)))
            return String(Base64.encode(encryptedBytes,Base64.DEFAULT))

        }catch (e:UnsupportedEncodingException){
            Log.e(TAG,"Encrypt fail",e)
            return plain
        }catch (e:NoSuchPaddingException){
            Log.e(TAG,"Encrypt fail",e)
            return plain
        }catch (e:NoSuchAlgorithmException){
            Log.e(TAG,"Encrypt fail",e)
            return plain
        }catch (e:InvalidKeyException){
            Log.e(TAG,"Encrypt fail",e)
            return plain
        }catch (e:IllegalBlockSizeException){
            Log.e(TAG,"Encrypt fail",e)
            return plain
        }catch (e:BadPaddingException){
            Log.e(TAG,"Encrypt fail",e)
            return plain
        }
    }

    //복호화(get)
    //데이터가 유출되더라도 복호화는 이 로직에서만 가능함

    fun decrypt(encryptedText:String):String{
        try{
            var cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE,(keyEntry as KeyStore.PrivateKeyEntry).privateKey)
            var base64Bytes = encryptedText.toByteArray(charset("UTF-8"))
            var decryptedBytes = Base64.decode(base64Bytes,Base64.DEFAULT)

            Log.d(TAG, "Decrypted Text: " + String(cipher.doFinal(decryptedBytes)))

            return String(cipher.doFinal(decryptedBytes))
        }catch (e:UnsupportedEncodingException){
             Log.e(TAG,"Encrypt fail",e)
            return encryptedText
        }catch (e:NoSuchPaddingException){
            Log.e(TAG,"Encrypt fail",e)
            return encryptedText
        }catch (e:NoSuchAlgorithmException){
            Log.e(TAG,"Encrypt fail",e)
            return encryptedText
        }catch (e:InvalidKeyException){
            Log.e(TAG,"Encrypt fail",e)
            return encryptedText
        }catch (e:IllegalBlockSizeException){
            Log.e(TAG,"Encrypt fail",e)
            return encryptedText
        }catch (e:BadPaddingException){
            Log.e(TAG,"Encrypt fail",e)
            return encryptedText
        }
    }
}