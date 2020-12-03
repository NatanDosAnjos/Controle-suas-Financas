package com.example.organizze.helper

import java.util.*

class CustomDate (calendar: Calendar) {
    private val myCalendar = calendar
    var day     = myCalendar[Calendar.DAY_OF_MONTH].toString()
    var year    = myCalendar[Calendar.YEAR].toString()
    private var monthStartedAtZero = myCalendar[Calendar.MONTH]

    var monthStartedAtOne = monthStartedAtZero+1

    fun getFormattedDate(): String {
        return "${myCalendar[Calendar.DAY_OF_MONTH]}/$monthStartedAtOne/${myCalendar[Calendar.YEAR]}"
    }

    fun getYearMonth(): String {
        return "$year/$monthStartedAtOne"
    }
}