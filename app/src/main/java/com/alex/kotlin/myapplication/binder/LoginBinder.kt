package com.alex.kotlin.myapplication.binder

import android.os.RemoteCallbackList
import android.util.Log
import com.alex.kotlin.myapplication.ILoginCallback
import com.alex.kotlin.myapplication.User

class LoginBinder : ILoginBinder.Stub() {

    private val remoteCallbackList = RemoteCallbackList<ILoginCallback>()

    override fun registerListener(iLoginCallback: ILoginCallback?) {
        remoteCallbackList.register(iLoginCallback)
    }

    override fun unregisterListener(iLoginCallback: ILoginCallback?) {
    remoteCallbackList.unregister(iLoginCallback)
    }

    private  var  user: User = User("User")

    override fun login(name: String?, pasd: String?) {
        Log.e("======","name = $name ; pasd = $pasd")
        if (name != null && pasd != null){
            user = User(name)
            val number = remoteCallbackList.beginBroadcast()
            for (i in 0 until number){
                remoteCallbackList.getBroadcastItem(i).loginSuccess()
            }
            remoteCallbackList.finishBroadcast()
        }else{
            val number = remoteCallbackList.beginBroadcast()
            for (i in 0 until number){
                remoteCallbackList.getBroadcastItem(i).loginFailed()
            }
            remoteCallbackList.finishBroadcast()
        }
    }

    override fun isLogin(): Boolean {
       return user != null
    }

    override fun getUser(): User? {
        return user
    }

}