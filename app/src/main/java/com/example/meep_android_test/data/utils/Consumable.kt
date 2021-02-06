package com.example.meep_android_test.data.utils

import java.util.concurrent.atomic.AtomicBoolean

class Consumable<T>(private val data: T) {

    private val isConsumed = AtomicBoolean(false)

    fun consume(action: (T) -> Unit) {
        if (isConsumed.compareAndSet(false, true)) {
            action(data)
        }
    }
}