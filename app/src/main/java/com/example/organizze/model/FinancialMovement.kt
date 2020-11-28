package com.example.organizze.model

import com.google.firebase.database.Exclude
import java.util.*

class FinancialMovement {
    companion object {
        @JvmStatic val FIRST_CHILD = "financialMovements"
        @JvmStatic val INCOME_OR_EXPENSE_KEY = "incomeOrExpense"
        @JvmStatic val VALUE_KEY = "value"
        @JvmStatic val CATEGORY_KEY = "category"
        @JvmStatic val DESCRIPTION_KEY = "description"
        @JvmStatic val YEAR_MONTH_KEY = "year_month"
        //@JvmStatic val YEAR_KEY = "year"
        @JvmStatic val DAY_KEY = "day"

    }

    @get:Exclude var calendar: Calendar = Calendar.getInstance()
        set(value) {
            field = value
            day = calendar[Calendar.DAY_OF_MONTH].toString()
            month = (calendar[Calendar.MONTH]).toString()
            year = calendar[Calendar.YEAR].toString()
            yearMonth = getMonthAndYearString()
        }
    var incomeOrExpense = "N"
    var value = 0.0
    var category = ""
    var description = ""
    var month = ""
        private set
    var year = ""
        private set
    var day = ""
    var yearMonth = ""
        set(value) {
            field = value
            val array = value.split("/")
            if (array.size > 1) {
                year = array[0]
                month = array[1]
            }
        }
    @get:Exclude var userId = ""


    @Exclude
    private fun getMonthAndYearString(): String {
        return "$year/$month"
    }

    @Exclude
    fun isExpense(): Boolean {
        return incomeOrExpense == "E" || incomeOrExpense == "e"
    }


}