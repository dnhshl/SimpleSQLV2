package com.example.simplesqlv2.roomdb

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


val dateformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun convertDateStringToLong(dateString: String): Long {
    return try {
        LocalDateTime.parse(dateString, dateformatter)
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
    }  catch (e : Exception) {
        e.printStackTrace()
        0L
    }

}

fun convertLongToDateString(date: Long): String {
    return Instant.ofEpochSecond(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(dateformatter)
}
