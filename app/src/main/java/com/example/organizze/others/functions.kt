package com.example.organizze.others

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*


fun showToast(context: Context, text: String = "") {
    if (text.isNotEmpty()) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

fun showSnackbar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

fun getLocale(resources: Resources): Locale {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) resources.configuration.locales.get(0)
        else resources.configuration.locale
}