// IConsumeBuyCallback.aidl
package com.alex.kotlin.myapplication.binder;

// Declare any non-default types here with import statements
import com.alex.kotlin.myapplication.Product;

interface IConsumeBuyCallback {

void buySuccess(in Product product);
void buyFailed(String message);

}
