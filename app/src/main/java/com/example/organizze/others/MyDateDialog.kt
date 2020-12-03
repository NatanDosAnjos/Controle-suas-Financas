package com.example.organizze.others

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import com.example.organizze.R
import com.example.organizze.helper.CustomDate
import kotlinx.android.synthetic.main.content_income_expenses.*
import java.util.*
import java.util.Calendar.*


class MyDateDialog(private val context: Context, editText: Int) : View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private var calendar: Calendar = getInstance(Locale.getDefault())
    var customDate = CustomDate(calendar)
    private val activity = context as Activity
    private val realEditText = activity.findViewById<EditText>(editText)
    private var dialog: DatePickerDialog

    init {
        dialog = DatePickerDialog(context,this , calendar[YEAR], calendar[MONTH], calendar[DAY_OF_MONTH])
        realEditText.setOnClickListener(this)
        realEditText.showSoftInputOnFocus = false
        realEditText.isCursorVisible = false
        realEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                choseDialogTheme()
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(realEditText.windowToken, 0)
                dialog.show()
            }

        }
    }

    private fun choseDialogTheme() {
        dialog = when {
            activity.checkBoxIncome.isChecked -> {
                DatePickerDialog(context, R.style.DateDialogIncome, this, calendar[YEAR], calendar[MONTH], calendar[DAY_OF_MONTH])
            }
            activity.checkBoxExpense.isChecked -> {
                DatePickerDialog(context, R.style.DateDialogExpense, this, calendar[YEAR], calendar[MONTH], calendar[DAY_OF_MONTH])
            }
            else -> {
                DatePickerDialog(context, R.style.DateDialogNothing, this, calendar[YEAR], calendar[MONTH], calendar[DAY_OF_MONTH])
            }
        }
    }

    private fun updateViewText() {
        realEditText.setText(customDate.getFormattedDate())
    }


    /*--------------------------------------- LISTENERS ----------------------------------------*/
    override fun onClick(v: View) {
        choseDialogTheme()
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        calendar[DAY_OF_MONTH] = day
        calendar[MONTH] = month
        calendar[YEAR] = year
        customDate = CustomDate(calendar)

        updateViewText()
    }
}