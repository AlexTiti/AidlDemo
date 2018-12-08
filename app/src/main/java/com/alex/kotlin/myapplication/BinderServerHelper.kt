package com.alex.kotlin.myapplication

import android.content.*
import android.os.IBinder
import com.alex.kotlin.myapplication.binder.BinderMangerService
import com.alex.kotlin.myapplication.binder.IBinderManger
import java.util.concurrent.CountDownLatch

class BinderServerHelper {

    var binderSuccessCallback : BinderSuccessCallback? = null

   private val countDownLatch by lazy {
        CountDownLatch(1)
    }

    val serviceConnect  = ServiceConnect()

    var iBinderManger : IBinderManger? = null

    var deathRecipient : IBinder.DeathRecipient? = null


    fun bindService(context: Context){

        deathRecipient = IBinder.DeathRecipient {
            iBinderManger?.asBinder()?.unlinkToDeath(deathRecipient,0)
            iBinderManger = null
        }

        val contextWrapper = ContextWrapper(context.applicationContext)

        runOnThread {
            val intent = Intent(contextWrapper, BinderMangerService::class.java)
            contextWrapper.bindService(intent, serviceConnect,Context.BIND_AUTO_CREATE)
            countDownLatch.await()
            binderSuccessCallback?.success()
        }

    }


   inner class ServiceConnect : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iBinderManger = IBinderManger.Stub.asInterface(service)
            service?.linkToDeath(deathRecipient,0)
            countDownLatch.countDown()
        }
    }

     fun unregister(context: Context){
         context.unbindService(serviceConnect)
     }

}