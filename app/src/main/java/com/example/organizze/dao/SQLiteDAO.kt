package com.example.organizze.dao

import android.content.ContentValues
import android.content.Context
import com.example.organizze.helper.SQLiteConnection
import com.example.organizze.model.FinancialMovement

class SQLiteDAO(context: Context) : FinancialMovementDAO {

    companion object {
        private var instanceAlreadyExist = false

        // Creates a singleton of attribute connection
        @JvmStatic
        private var connection: SQLiteConnection? = null
            set(value) {
                if (!instanceAlreadyExist && value != null) {
                    field = value
                    instanceAlreadyExist = true
                }
            }
    }

    init {
        connection = SQLiteConnection(context)
    }

    override fun saveMovement(financialMovement: FinancialMovement) : Long {
        val writable = connection?.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(FinancialMovement.DAY_KEY, financialMovement.day)
        contentValues.put(FinancialMovement.YEAR_KEY, financialMovement.year)
        contentValues.put(FinancialMovement.MONTH_KEY, financialMovement.month)
        contentValues.put(FinancialMovement.VALUE_KEY, financialMovement.value)
        contentValues.put(FinancialMovement.CATEGORY_KEY, financialMovement.category)
        contentValues.put(FinancialMovement.DESCRIPTION_KEY, financialMovement.description)
        contentValues.put(FinancialMovement.INCOME_OR_EXPENSE_KEY, financialMovement.incomeOrExpense)
        return writable?.insert(connection?.tableName, null, contentValues)?: 0L

    }

    override fun getMovement() {
        val readable = connection?.readableDatabase

    }

    override fun deleteMovement(financialMovement: FinancialMovement) {

    }

    override fun updateMovement(old: FinancialMovement, new: FinancialMovement) {


    }


}
