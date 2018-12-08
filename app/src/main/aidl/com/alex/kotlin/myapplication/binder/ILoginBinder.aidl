// ILoginBinder.aidl
package com.alex.kotlin.myapplication.binder;

// Declare any non-default types here with import statements
import com.alex.kotlin.myapplication.User;
import com.alex.kotlin.myapplication.ILoginCallback;

interface ILoginBinder {

 void login(String name ,String pasd);

 boolean isLogin();

 User getUser();

 void registerListener(ILoginCallback iLoginCallback);

 void unregisterListener(ILoginCallback iLoginCallback);

}
