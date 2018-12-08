package com.alex.kotlin.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.alex.kotlin.myapplication.binder.IAddProductCallback
import com.alex.kotlin.myapplication.binder.IProductBinder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

class ProfuctActivity : AppCompatActivity(), BinderSuccessCallback {

    private val binderServerHelper = BinderServerHelper()

    private val iAddProductCallback= object : IAddProductCallback.Stub(){

            override fun addSuccess(product: Product?){
                val string = java.lang.StringBuilder(tvAdd.text.toString())
                string.append("\n")
                string.append(product?.name)
                string.append(product?.number)
                string.append(product?.price)
                tvAdd.text = string.toString()
            }

            override fun addFailed(message : String ) {
              Toast.makeText(this@ProfuctActivity,message,Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binderServerHelper.binderSuccessCallback = this
        binderServerHelper.bindService(this@ProfuctActivity)

        button.setOnClickListener {
            val name = textView4.text.trim().toString()
            val price = textView5.text.trim().toString()
            val number = textView6.text.trim().toString()

            if (name == null || price == null || number == null){
                return@setOnClickListener
            }else{
                productBinder?.addProduct(Product(name,price.toFloat(),number.toInt()))
            }
        }

        buttonQuery.setOnClickListener {
            var stringText  = StringBuilder()
            val map : HashMap<String,Product> = productBinder?.product as HashMap<String, Product>
            for (bean in map.keys){
                stringText.append(bean)
                stringText.append(map[bean]!!.price)
                stringText.append(map[bean]!!.number)
                stringText.append("\n")
            }
            tvRefresh.text = stringText.toString()
        }

        btnGoBuy.setOnClickListener {
            startActivity(Intent(this,ConsumeActivity::class.java))
        }
        val file = File(cacheDir,"f.txt")
        val input = ObjectInputStream(FileInputStream(file))
        val book: Book = input.readObject() as Book
        Log.e("============","${book.name} === ${book.age}")
        input.close()

    }

    var productBinder : IProductBinder? = null
    override fun success() {

        val productIBinder = binderServerHelper.iBinderManger?.queryBinder(1)
        productBinder = IProductBinder.Stub.asInterface(productIBinder)
        productBinder?.registerCallBack(iAddProductCallback)

        productBinder?.addProduct(Product("苹果",10.toFloat(),100))
        productBinder?.addProduct(Product("桃子",5.toFloat(),50))
        productBinder?.addProduct(Product("香蕉",15.toFloat(),150))

    }

    override fun deathToTryBindAgain() {
        binderServerHelper.bindService(this)
        binderServerHelper.binderSuccessCallback = this
    }

    override fun onDestroy() {
        productBinder?.unregisterCallBack(iAddProductCallback)
        super.onDestroy()
        binderServerHelper.unregister(this)
    }

}
