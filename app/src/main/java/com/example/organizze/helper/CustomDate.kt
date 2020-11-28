package com.example.organizze.helper

import java.text.SimpleDateFormat
import java.util.*

class CustomDate (millis: Long) {

    private var date: Calendar = Calendar.getInstance()
    private var timeOfFinancialMovement: Long

    init {
        timeOfFinancialMovement = millis
    }

    constructor(calendarInstance: Calendar = Calendar.getInstance()) : this(calendarInstance.timeInMillis) {
        this.date = calendarInstance
    }

    fun setDateFromString(date: String) {
        val partsOfDate = date.split("/")
        println("setDateFromString: $partsOfDate")

        this.date.set(partsOfDate[2].toInt(), partsOfDate[1].toInt(), partsOfDate[0].toInt())
    }

    fun getFormattedDate(): String {
        timeOfFinancialMovement = System.currentTimeMillis()

        return SimpleDateFormat("dd/MM/yyyy", Locale.CANADA).format(timeOfFinancialMovement)
    }

    fun getDay(): String {
        return date.get(Calendar.DAY_OF_MONTH).toString()
    }

    fun getMonth(): String {
        return date.get(Calendar.MONTH).toString()
    }

    fun getYear(): String {
        return date.get(Calendar.YEAR).toString()
    }
}