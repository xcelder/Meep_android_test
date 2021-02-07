package com.example.meep_android_test.utils

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

fun Drawable.tint(@ColorInt tintColor: Int, tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_ATOP) =
    apply {
        setTintList(ColorStateList.valueOf(tintColor))
        setTintMode(tintMode)
    }