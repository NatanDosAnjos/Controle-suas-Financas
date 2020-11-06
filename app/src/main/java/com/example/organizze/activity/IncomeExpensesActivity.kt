package com.example.organizze.activity


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.organizze.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.text.SimpleDateFormat

class IncomeExpensesActivity : AppCompatActivity() {

    private lateinit var linearLayout: ConstraintLayout
    private lateinit var viewTextValue: TextView
    private lateinit var checkBoxIncome: CheckBox
    private lateinit var checkBoxExpense: CheckBox
    private lateinit var viewValue: EditText
    private lateinit var viewDate: EditText
    private lateinit var viewDescription: EditText
    private lateinit var fab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_expenses)

        linearLayout = findViewById(R.id.secondConstraintLayout)
        viewTextValue = findViewById(R.id.textViewShowValue)
        checkBoxIncome = findViewById(R.id.checkBoxIncome)
        checkBoxExpense = findViewById(R.id.checkBoxExpense)
        viewValue = findViewById(R.id.editTextValue)
        viewDate = findViewById(R.id.editTextDate)
        viewDescription = findViewById(R.id.editTextDescription)
        fab = findViewById(R.id.FAB_Ok)

        val currentDate = System.currentTimeMillis()
        val dateInString = SimpleDateFormat("dd/MM/yyyy", resources.configuration.locale).format(currentDate)
        viewDate.setText(dateInString)

        initViewValueFeatures(viewValue)

        checkBoxExpense.setOnClickListener {
            checkBoxIncome.isChecked = false
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.expenseAccent))
            linearLayout.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.expensePrimaryDark))
            window.statusBarColor = resources.getColor(R.color.expensePrimaryDark)
            checkBoxIncome.isClickable = true
            checkBoxExpense.isClickable = false
        }

        checkBoxIncome.setOnClickListener {
            checkBoxExpense.isChecked = false
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.incomeAccent))
            linearLayout.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.incomePrimary))
            window.statusBarColor = resources.getColor(R.color.incomePrimaryDark)
            checkBoxIncome.isClickable = false
            checkBoxExpense.isClickable = true
        }

        fab.setOnClickListener {
            if(checkBoxExpense.isChecked) {


            } else {

            }
            finish()
        }


    }

    private fun initViewValueFeatures(myViewValue: EditText) {
        val valueReceiver  = NumberFormat.getInstance(resources.configuration.locale)

        showValue(viewTextValue, 0.00)
        myViewValue.setOnFocusChangeListener { _, b ->
            if (b) {
                val text = myViewValue.text.toString()
                if(text.isNotEmpty()) {
                    val textWithoutPrefix = text.replace(".", "")
                    val number = valueReceiver.parse(textWithoutPrefix) as Double
                    showValue(viewTextValue, number)
                }

            } else {
                val value = myViewValue.text.toString()
                if(value.isNotEmpty()) {
                    val number = value.toDouble()
                    showValue(viewTextValue, number)
                }
                myViewValue.setText("")
            }
        }
    }


    private fun showValue(myViewValue: TextView, value: Double) {
        val currentLocale = resources.configuration.locale
        val toFormatValue = NumberFormat.getCurrencyInstance(currentLocale)
        myViewValue.text  = toFormatValue.format(value)
    }


}