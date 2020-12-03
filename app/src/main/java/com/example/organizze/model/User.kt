package com.example.organizze.model

import com.google.firebase.database.Exclude

open class User (var name: String, var email: String ){
    companion object {
        @JvmStatic val FIRST_CHILD = "users"
        @JvmStatic val STRING_TOTAL_INCOME = "totalIncome"
        @JvmStatic val STRING_TOTAL_EXPENSES = "totalExpenses"
    }

    var totalExpenses = 0.0
    var totalIncome = 0.0
    @get:Exclude var password = ""
    @get:Exclude var userId: String = ""

    constructor() : this("", "")
}