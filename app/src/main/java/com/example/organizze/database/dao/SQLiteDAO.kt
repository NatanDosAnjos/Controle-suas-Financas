package com.example.organizze.database.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.organizze.activity.FirstActivity.Companion.listOfFinancialMovement
import com.example.organizze.database.SQLiteConnection
import com.example.organizze.model.FinancialMovement
import com.example.organizze.others.getLocale
import java.text.NumberFormat

class SQLiteDAO(context: Context, userId: String) : FinancialMovementDAO {

    companion object {
        private var instanceAlreadyExist = false

        // Creates a singleton of attribute connection
        @JvmStatic
        private var connection: SQLiteConnection? = null
            set(value) {
                if (!instanceAlreadyExist) {
                    field = value
                    if(value != null) {
                        instanceAlreadyExist = true
                    }
                }
            }

        @JvmStatic
        fun disconnect() {
            connection = null
            instanceAlreadyExist = false
            listOfFinancialMovement.clear()
        }
    }

    init {
        connection = SQLiteConnection(context, userId)
    }

    override fun saveMovement(financialMovement: FinancialMovement) : Long {
        val writable = connection?.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(FinancialMovement.DAY_KEY, financialMovement.day)
        contentValues.put(FinancialMovement.YEAR_MONTH_KEY, financialMovement.yearMonth)
        contentValues.put(FinancialMovement.VALUE_KEY, financialMovement.value)
        contentValues.put(FinancialMovement.CATEGORY_KEY, financialMovement.category)
        contentValues.put(FinancialMovement.DESCRIPTION_KEY, financialMovement.description)
        contentValues.put(FinancialMovement.INCOME_OR_EXPENSE_KEY, financialMovement.incomeOrExpense)
        val thisReturn = writable?.insert(connection?.tableName, null, contentValues)?: 0L
        println("Salvando na tabela: ${connection?.tableName}")
        writable?.close()
        return thisReturn

    }

    override fun getMovements(yearMonth: String, notifyAdapterRunnable: Runnable?) {
        val readable = connection?.readableDatabase

        val where = "${FinancialMovement.YEAR_MONTH_KEY} = ?"
        val selectionArgs = arrayOf(yearMonth)
        val setOrder = "${FinancialMovement.YEAR_MONTH_KEY} DESC"

        var cursor: Cursor? = null
        try {
            cursor = readable?.query(
                connection!!.tableName,
                null,           // Recupera todas as colunas
                where,                  // Recupera as linhas caso a coluna Where tenha o valor expecificado no próximo parâmetro
                selectionArgs,          // Valor de where
                null,
                null,
                setOrder)               // Ordena os dados em ordem definida


        } catch (e: Exception) {
            connection?.createTable(connection!!.writableDatabase)
            listOfFinancialMovement.clear()
        }

        if (cursor != null) {
            listOfFinancialMovement.clear()
            with(cursor) {
                while (moveToNext()) {
                    val value = this.getDouble(this.getColumnIndex(FinancialMovement.VALUE_KEY))
                    val incomeOrExpense =
                        this.getString(this.getColumnIndex(FinancialMovement.INCOME_OR_EXPENSE_KEY))
                    val category =
                        this.getString(this.getColumnIndex(FinancialMovement.CATEGORY_KEY))
                    val description =
                        this.getString(this.getColumnIndex(FinancialMovement.DESCRIPTION_KEY))
                    val yearMonthValue =
                        this.getString(this.getColumnIndex(FinancialMovement.YEAR_MONTH_KEY))
                    val day = this.getString(this.getColumnIndex(FinancialMovement.DAY_KEY))
                    val id = this.getString(this.getColumnIndex(FinancialMovement.ID_KEY))

                    listOfFinancialMovement.add(instantiateFinancialMovement(
                        value,
                        incomeOrExpense,
                        category,
                        description,
                        yearMonthValue,
                        day,
                        id
                    ))
                }
            }
            notifyAdapterRunnable?.run()
        }

        cursor?.close()
    }

    override fun deleteMovement(financialMovement: FinancialMovement): Boolean {
        val writable = connection!!.writableDatabase
        val where = "${FinancialMovement.ID_KEY} LIKE ?"
        val args = arrayOf(financialMovement.id)
        val result = writable.delete(connection!!.tableName, where, args)

        return result > 0
    }

    override fun updateMovement(old: FinancialMovement, new: FinancialMovement) {


    }

    private fun instantiateFinancialMovement(
        value: Double,
        incomeOrExpense: String,
        category: String,
        description: String,
        yearMonth: String,
        day: String,
        id: String
    ): FinancialMovement {
        val financialMovement = FinancialMovement()
        financialMovement.value = value
        financialMovement.incomeOrExpense = incomeOrExpense
        financialMovement.yearMonth = yearMonth
        financialMovement.category = category
        financialMovement.description = description
        financialMovement.day = day
        financialMovement.id = id

        return financialMovement
    }
}
