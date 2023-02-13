package com.example.mvvmarch.arch.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

fun Context.isNetworkAvailable(): Boolean = applicationContext.isNetworkAvailable()

fun Application.isNetworkAvailable(): Boolean =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo!!.isAvailable