package com.example.organizze.model

import com.google.firebase.database.Exclude

open class User (){
    companion object {
        @JvmStatic val FIRST_CHILD = "users"
        @JvmStatic val STRING_TOTAL_INCOME = "totalIncome"
        @JvmStatic val STRING_TOTAL_EXPENSES = "totalExpenses"
    }

    var name = ""
    var email = ""
    var totalExpenses = 0.0
    var totalIncome = 0.0
    @get:Exclude var password = ""
    @get:Exclude var userId: String = ""

    constructor(name: String, email: String) : this() {
        this.name = name
        this.email = email
    }

    fun getBalance(): Double {
        return totalIncome - totalExpenses
    }
}