package ru.chernakov.appmonitor.data.utils

import java.text.SimpleDateFormat
import java.util.*


fun formatDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm")
    return dateFormat.format(date)
}

@Throws(NullPointerException::class)
fun getUnixTimestamp(dt: Date): Int {
    return convertMillisToTimestamp(dt.time)
}

fun getDateFromUnixTimestamp(ts: Int): Date {
    return Date(ts * 1000L)
}

fun getCurrentUnixTimestamp(): Int {
    return convertMillisToTimestamp(System.currentTimeMillis())
}

fun convertMillisToTimestamp(timeMillis: Long): Int {
    return (timeMillis / 1000L).toInt()
}

fun convertTimestampToMillis(timestamp: Int): Long {
    return timestamp * 1000L
}