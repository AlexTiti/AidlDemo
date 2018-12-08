package com.alex.kotlin.myapplication.binder

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.util.Log

class BinderMangerService : Service() {

    val loginBinder = LoginBinder()
    val productBinder = ProductBinder()

    val binder = IBinderMangerImpl()

    override fun onBind(intent: Intent) : IBinder?{
        val check = checkCallingOrSelfPermission("com.alex.kotlin.myapplication.permissions.BINDER_SERVICE")
        if (check == PackageManager.PERMISSION_DENIED){
            return  null
        }
      return  binder
    }

   inner class IBinderMangerImpl : IBinderManger.Stub(){

       override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {

           val check = checkCallingOrSelfPermission("com.alex.kotlin.myapplication.permissions.BINDER_SERVICE")
           if (check == PackageManager.PERMISSION_DENIED){
               return false
           }

          val packages = packageManager.getPackagesForUid(Binder.getCallingUid())
           if (packages != null && !packages.isEmpty()){
               val packageName = packages[0]
               if (!packageName.startsWith("com.alex")){
                   return false
               }
           }
           return super.onTransact(code, data, reply, flags)
       }


        override fun queryBinder(code: Int): IBinder {

          return  when(code){
                0 -> loginBinder
                else -> productBinder
            }
        }
    }
}
