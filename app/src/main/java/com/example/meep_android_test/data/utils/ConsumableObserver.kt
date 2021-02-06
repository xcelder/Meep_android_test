package com.example.meep_android_test.data.utils

import androidx.lifecycle.Observer

class ConsumableObserver<T>(private val action: (T) -> Unit) : Observer<Consumable<T>> {
    override fun onChanged(consumable: Consumable<T>?) {
        consumable?.consume { action(it) }
    }
}