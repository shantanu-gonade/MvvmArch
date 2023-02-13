package com.example.mvvmarch.arch.ext

import android.content.Context
import android.widget.Toast

fun Context.toast(value: String) {
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}