package com.example.mvvmarch.arch.ext

import android.content.Context
import android.content.SharedPreferences

const val SP_NAME_DEFAULT = "default_sp"

fun Context.sharedPreferences(spName: String = SP_NAME_DEFAULT): SharedPreferences =
    getSharedPreferences(spName, Context.MODE_PRIVATE)