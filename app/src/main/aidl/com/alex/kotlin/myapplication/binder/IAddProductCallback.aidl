// IAddProductCallback.aidl
package com.alex.kotlin.myapplication.binder;

// Declare any non-default types here with import statements
import com.alex.kotlin.myapplication.Product;

interface IAddProductCallback {
 void addSuccess(in Product product);
 void addFailed(String message);
}
