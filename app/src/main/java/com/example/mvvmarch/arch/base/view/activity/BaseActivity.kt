package com.example.mvvmarch.arch.base.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmarch.arch.base.view.IView
import com.example.mvvmarch.databinding.ActivityMainBinding

abstract class BaseActivity: AppCompatActivity(), IView {

    abstract val layoutId: Int

    abstract var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}