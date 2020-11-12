package com.example.organizze.helper

import java.text.SimpleDateFormat
import java.util.*

class CustomDate (millis: Long) {

    private var date: Date = Date()
    private var timeOfFinancialMovement = 0L

    init {
         this.timeOfFinancialMovement = millis
    }

    constructor(date: Date) : this(date.time) {
        this.date = date
    }


    fun getFormattedDate(locale: Locale): String {
        timeOfFinancialMovement = System.currentTimeMillis()

        return SimpleDateFormat("dd/MM/yyyy", locale).format(timeOfFinancialMovement)
    }

    fun getMonthAndDay(): String {
        return SimpleDateFormat("MMdd", Locale("PT", "BR")).format(timeOfFinancialMovement)
    }

    fun getYear():String {
        return SimpleDateFormat("yyyy", Locale("pt", "BR"))
            .format(timeOfFinancialMovement)
    }

    fun dateToMillis(date: String): Long {
        return Date.parse(date)
    }
}