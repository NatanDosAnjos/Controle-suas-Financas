package com.example.organizze.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.organizze.model.FinancialMovement
const val dbName = "Oraganizze.db"
const val version = 1

class SQLiteConnection(context: Context) : SQLiteOpenHelper(context, dbName, null, version) {

    val tableName = "Financial_Movement"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $tableName(" +
                "${FinancialMovement.VALUE_KEY} INTEGER," +
                "${FinancialMovement.CATEGORY_KEY} TEXT," +
                "${FinancialMovement.YEAR_KEY} TEXT," +
                "${FinancialMovement.MONTH_KEY} TEXT," +
                "${FinancialMovement.DAY_KEY} TEXT," +
                "${FinancialMovement.DESCRIPTION_KEY} TEXT," +
                "${FinancialMovement.INCOME_OR_EXPENSE_KEY} VARCHAR(1))")

        Log.i("DB", "OnCreateDb")
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


    }
}