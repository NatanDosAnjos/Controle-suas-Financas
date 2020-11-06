package com.example.organizze.others

import android.content.Context
import android.widget.Toast


fun showToast(context: Context, text: String = "") {
    if (text.isNotEmpty()) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}