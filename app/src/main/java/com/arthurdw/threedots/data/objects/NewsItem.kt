package com.arthurdw.threedots.data.objects

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NewsItem(
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val source: String,
    val dateStr: String,
) {
    val date: Date
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            return formatter.parse(dateStr) ?: throw IllegalArgumentException("Date is null")
        }
}
