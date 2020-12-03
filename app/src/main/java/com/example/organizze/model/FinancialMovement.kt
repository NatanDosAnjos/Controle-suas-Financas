package com.example.organizze.model

import com.example.organizze.helper.CustomDate
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

    @get:Exclude var customDate = CustomDate(Calendar.getInstance())
        set (value) {
            yearMonth = value.getYearMonth()
            month = value.monthStartedAtOne.toString()
            year = value.year
            day = value.day
            field = value
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

    @get:Exclude var userId = ""

    @Exclude
    fun isExpense(): Boolean {
        return incomeOrExpense == "E" || incomeOrExpense == "e"
    }


}