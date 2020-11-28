package com.example.organizze.dao

import android.content.Context
import com.example.organizze.model.FinancialMovement

interface FinancialMovementDAO {

    fun saveMovement(financialMovement: FinancialMovement) : Long

    fun getMovement()

    fun deleteMovement(financialMovement: FinancialMovement)

    fun updateMovement(old: FinancialMovement, new: FinancialMovement)
}