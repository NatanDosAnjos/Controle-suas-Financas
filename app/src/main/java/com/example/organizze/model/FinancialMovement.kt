package com.example.organizze.model

import com.example.organizze.helper.CustomDate
import com.google.firebase.database.Exclude

class FinancialMovement {
    companion object {
        @JvmStatic val FIRST_CHILD = "financialMovements"
    }

    var incomeOrExpense = "N"
    var value = 0.0
    var category = ""
    var description = ""
    var date = ""
    @get:Exclude var userId = ""
    @get:Exclude var customDate: CustomDate? = null

    fun isExpense(): Boolean {
        return incomeOrExpense == "E" || incomeOrExpense == "e"
    }

}