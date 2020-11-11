package com.example.organizze.activity


import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.database.DataBase
import com.example.organizze.helper.CustomDate
import com.example.organizze.model.FinancialMovement
import com.example.organizze.others.getLocale
import com.example.organizze.others.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.util.*

class IncomeExpensesActivity : AppCompatActivity() {

    private lateinit var secondConstraintLayout: ConstraintLayout
    private lateinit var viewTextValue: TextView
    private lateinit var checkBoxExpense: CheckBox
    private lateinit var checkBoxIncome: CheckBox
    private lateinit var viewValue: EditText
    private lateinit var viewDescription: EditText
    private lateinit var viewCategory: EditText
    private lateinit var viewDate: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var financialMovement: FinancialMovement


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_expenses)

        secondConstraintLayout = findViewById(R.id.secondConstraintLayout)
        viewTextValue = findViewById(R.id.textViewShowValue)
        checkBoxExpense = findViewById(R.id.checkBoxExpense)
        checkBoxIncome = findViewById(R.id.checkBoxIncome)
        viewValue = findViewById(R.id.editTextValue)
        viewCategory = findViewById(R.id.editTextCategory)
        viewDescription = findViewById(R.id.editTextDescription)
        viewDate = findViewById(R.id.editTextDate)
        fab = findViewById(R.id.FAB_Ok)
        financialMovement = FinancialMovement()
        financialMovement.userId = FirebaseConfiguration.getAuthentication().currentUser!!.uid


        initViewValueFeatures(viewValue)

        viewDate.setOnFocusChangeListener { _, b ->
            if(b && viewDate.text.toString().isEmpty()) {
                val dateInString = CustomDate(System.currentTimeMillis()).getFormattedDate(getLocale(resources))
                viewDate.setText(dateInString)
            }
        }

        checkBoxExpense.setOnClickListener {
            financialMovement.incomeOrExpense = "E"
            checkBoxIncome.isChecked = false
            checkBoxIncome.isClickable = true
            checkBoxExpense.isClickable = false

            //Change Colors of Activity
            fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.expenseAccent))
            secondConstraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.expensePrimaryDark))
            window.statusBarColor = ContextCompat.getColor(this, R.color.expensePrimaryDark)
        }

        checkBoxIncome.setOnClickListener {
            financialMovement.incomeOrExpense = "I"
            checkBoxExpense.isChecked = false
            checkBoxIncome.isClickable = false
            checkBoxExpense.isClickable = true

            //Change Colors of Activity
            fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.incomeAccent))
            secondConstraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.incomePrimaryDark))
            window.statusBarColor = ContextCompat.getColor(this, R.color.incomePrimaryDark)
        }

        fab.setOnClickListener {
            if(!checkBoxExpense.isChecked && !checkBoxIncome.isChecked) {
                showToast(this, resources.getString(R.string.select_financial_movement_type))
            } else if(financialMovement.value == 0.0){
                showToast(this, resources.getString(R.string.type_value))
            } else if(viewDate.text.toString().isEmpty()) {
                showToast(this, getString(R.string.type_date))
            } else {
                financialMovement.category = viewCategory.text.toString()
                financialMovement.date = viewDate.text.toString()
                financialMovement.description = viewDescription.text.toString()
                financialMovement.customDate = CustomDate(Date(viewDate.text.toString()))
                DataBase.saveInDataBase(financialMovement)
                finish()
            }
        }
    }

    private fun initViewValueFeatures(myViewValue: EditText) {
        val valueReceiver  = NumberFormat.getInstance(getLocale(resources))

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
                    financialMovement.value = number
                    showValue(viewTextValue, number)
                }
                myViewValue.setText("")
            }
        }
    }


    private fun showValue(myViewValue: TextView, value: Double) {
        val currentLocale = getLocale(resources)
        val toFormatValue = NumberFormat.getCurrencyInstance(currentLocale)
        myViewValue.text  = toFormatValue.format(value)
    }


}