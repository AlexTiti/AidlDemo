// IProductBinder.aidl
package com.alex.kotlin.myapplication.binder;

// Declare any non-default types here with import statements
import com.alex.kotlin.myapplication.Product;
import com.alex.kotlin.myapplication.binder.IAddProductCallback;
import com.alex.kotlin.myapplication.binder.IConsumeBuyCallback;

interface IProductBinder {

   void addProduct(in Product product);

   Map getProduct();

   int productSize();

   void buyProduct(String name);

   void registerCallBack(IAddProductCallback iAddProductCallback);
   void unregisterCallBack(IAddProductCallback iAddProductCallback);

   void registerConsumeCallBack(IConsumeBuyCallback iConsumeBuyCallback);
   void unregisterConsumeCallBack(IConsumeBuyCallback iConsumeBuyCallback);

}
