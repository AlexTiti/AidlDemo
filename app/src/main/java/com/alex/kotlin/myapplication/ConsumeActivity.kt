package com.alex.kotlin.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.alex.kotlin.myapplication.binder.IConsumeBuyCallback
import com.alex.kotlin.myapplication.binder.ILoginBinder
import com.alex.kotlin.myapplication.binder.IProductBinder
import kotlinx.android.synthetic.main.activity_consume.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.text.StringBuilder

class ConsumeActivity : AppCompatActivity(), BinderSuccessCallback {

    private val binderServerHelper = BinderServerHelper()
    var  binderLogin : ILoginBinder? = null
    var  binderProduct : IProductBinder? = null

    private val iConsumeBuyCallback = object : IConsumeBuyCallback.Stub(){

        override fun buySuccess(product: Product?) {

            val string = StringBuilder()
            string.append(" ${binderLogin?.user?.name} 购买的商品：")
            string.append("\n")
                string.append(product?.name)
                string.append(product?.price)
                string.append(product?.number)
                string.append("\n")
                 textView.text = string.toString()
        }

        override fun buyFailed(message: String?) {
           Toast.makeText(this@ConsumeActivity,message,Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consume)

        binderServerHelper.bindService(this)
        binderServerHelper.binderSuccessCallback = this

        buttonRefresh.setOnClickListener {
            var stringText = StringBuilder()
            stringText.append("商品剩余：")
            stringText.append("\n")

            val map : HashMap<String, Product> = binderProduct?.product as HashMap<String, Product>
            for (bean in map.keys){
                stringText.append(bean)
                stringText.append(map[bean]!!.price)
                stringText.append(map[bean]!!.number)
                stringText.append("\n")
            }
            textView2.text = stringText.toString()
        }

        btnBuy.setOnClickListener {
            tvToBuy.text.trim().toString()?.let {
                binderProduct?.buyProduct(it)
            }
        }
    }

    override fun success() {
        btnBuy.isClickable = true

        val iBinderProduct = binderServerHelper.iBinderManger?.queryBinder(1)
        binderProduct = IProductBinder.Stub.asInterface(iBinderProduct)
        binderProduct?.registerConsumeCallBack(iConsumeBuyCallback)

        val iBinderLogin = binderServerHelper.iBinderManger?.queryBinder(0)
        binderLogin = ILoginBinder.Stub.asInterface(iBinderLogin)

    }

    override fun deathToTryBindAgain() {
        binderServerHelper.bindService(this)
        binderServerHelper.binderSuccessCallback = this
    }

    override fun onDestroy() {
        binderProduct?.unregisterConsumeCallBack(iConsumeBuyCallback)
        binderServerHelper.unregister(this)
        super.onDestroy()
    }
}
