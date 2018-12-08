package com.alex.kotlin.myapplication

import java.util.concurrent.Executors

val THREADPOOL = Executors.newSingleThreadExecutor()

fun runOnThread(f:() ->Unit){
    THREADPOOL.execute(f)
}