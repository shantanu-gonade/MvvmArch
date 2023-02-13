package com.example.mvvmarch.utils

import com.example.mvvmarch.app.BaseApplication
import com.example.mvvmarch.arch.ext.toast

fun toast(value: String): Unit =
    BaseApplication.INSTANCE.toast(value)
