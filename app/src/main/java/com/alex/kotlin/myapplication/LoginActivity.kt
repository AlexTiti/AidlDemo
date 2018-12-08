package com.alex.kotlin.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.alex.kotlin.myapplication.binder.ILoginBinder
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class LoginActivity : AppCompatActivity(),BinderSuccessCallback {

    var loginBinder  :  ILoginBinder? = null
    private val binderServerHelper = BinderServerHelper()

    private val loginCallback = object : ILoginCallback.Stub(){

        override fun loginSuccess() {
            Log.e("======","登陆成功")
                Toast.makeText(this@LoginActivity,"登陆成功",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity,ProfuctActivity::class.java))

        }
        override fun loginFailed() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binderServerHelper.binderSuccessCallback = this
        binderServerHelper.bindService(this@LoginActivity)

        btnLogin.text = "登陆"

        val book = Book("Android",20)
        val file = File(cacheDir,"f.txt")
        val out = ObjectOutputStream(FileOutputStream(file))
        out.writeObject(book)
        out.close()


    }

    override fun success() {

        val iBinder = binderServerHelper.iBinderManger?.queryBinder(0)
        loginBinder = ILoginBinder.Stub.asInterface(iBinder)
        loginBinder?.registerListener(loginCallback)

        btnLogin.setOnClickListener {
            runOnUiThread {
               val account =  tvAccount.text.trim().toString()
                val pasd = tvPswd.text.trim().toString()
                if (account == null || pasd == null){
                    Toast.makeText(this,"账号和密码不能为空！",Toast.LENGTH_SHORT).show()
                    return@runOnUiThread
                }
                loginBinder?.login(account,pasd)
            }
        }
    }

    override fun deathToTryBindAgain() {
        binderServerHelper.bindService(this)
        binderServerHelper.binderSuccessCallback = this
    }

    override fun onDestroy() {
        loginBinder?.unregisterListener(loginCallback)
        super.onDestroy()
        binderServerHelper.unregister(this)
    }
}
