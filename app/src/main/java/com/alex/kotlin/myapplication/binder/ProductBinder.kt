package com.alex.kotlin.myapplication.binder

import android.os.RemoteCallbackList
import com.alex.kotlin.myapplication.Product
import java.util.concurrent.ConcurrentHashMap

class ProductBinder : IProductBinder.Stub(){

    private val remoteCallbackList = RemoteCallbackList<IAddProductCallback>()
    private val remoteConsumeCallbackList = RemoteCallbackList<IConsumeBuyCallback>()

    override fun registerCallBack(iAddProductCallback: IAddProductCallback?) {
        remoteCallbackList.register(iAddProductCallback)
    }

    override fun unregisterCallBack(iAddProductCallback: IAddProductCallback?) {
        remoteCallbackList.unregister(iAddProductCallback)
    }

    private val listProduct : ConcurrentHashMap<String,Product> by lazy {
       ConcurrentHashMap<String,Product>()
    }

    override fun addProduct(product: Product?) {
       product?.let {
           if (listProduct.contains(product.name)){
               val product = listProduct.get(product.name)!!
               val number = product.number + product.number
               if (number <= 100){
                   product.number = number
                   toReportListener(true,product)
               }else{
                   toReportListener(false,product)
               }
           }else{
               listProduct.put(product.name,product)
               toReportListener(true,product)
           }
       }
     }

    override fun getProduct(): ConcurrentHashMap<String,Product> {
        return listProduct
    }

    override fun productSize() = listProduct.size

    override fun buyProduct(name: String) {
        val product = listProduct[name]
        if (product == null){
            toReportConsuneListener(false,product,name)
            return
        }
        product?.let {
            if (it.number > 1){
                product.number = it.number - 1
            }else{
                listProduct.remove(name)
            }
            toReportConsuneListener(true,product,name)
        }

    }

    private fun toReportConsuneListener(boolean: Boolean,product: Product? = null,name: String){
        val numberListener = remoteConsumeCallbackList.beginBroadcast()
        for (i in 0 until numberListener){
            if (boolean) {
                remoteConsumeCallbackList.getBroadcastItem(i).buySuccess(product!!)
            }else{
                remoteConsumeCallbackList.getBroadcastItem(i).buyFailed("${name} 已经卖完了！")
            }
        }
        remoteConsumeCallbackList.finishBroadcast()
    }

    private fun toReportListener(boolean: Boolean, product: Product){
        val numberListener = remoteCallbackList.beginBroadcast()
        for (i in 0 until numberListener){
            if (boolean) {
                remoteCallbackList.getBroadcastItem(i).addSuccess(product!!)
            }else{
                remoteCallbackList.getBroadcastItem(i).addFailed("${product.number} 还剩余 ${product.number},暂时不许添加！")
            }
        }
        remoteCallbackList.finishBroadcast()
    }

    override fun registerConsumeCallBack(iConsumeBuyCallback: IConsumeBuyCallback?) {
        remoteConsumeCallbackList.register(iConsumeBuyCallback)
    }

    override fun unregisterConsumeCallBack(iConsumeBuyCallback: IConsumeBuyCallback?) {
        remoteConsumeCallbackList.unregister(iConsumeBuyCallback)
    }

}