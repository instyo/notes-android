package com.example.mynotes.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.mynotes.R

class ColorUtil {
    fun listColor(context: Context): List<Int> {
        return listOf(
            ContextCompat.getColor(context, R.color.white),
            ContextCompat.getColor(context, R.color.orange),
            ContextCompat.getColor(context, R.color.blue),
            ContextCompat.getColor(context, R.color.pink),
            ContextCompat.getColor(context, R.color.green),
            ContextCompat.getColor(context, R.color.soft_red),
        )
    }
}