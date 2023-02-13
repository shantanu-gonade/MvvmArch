package com.example.mvvmarch.arch.function

typealias Supplier<T> = () -> T

interface Consumer<T> {

    fun accept(t: T)
}