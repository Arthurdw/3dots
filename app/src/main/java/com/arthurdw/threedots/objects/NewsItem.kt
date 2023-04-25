package com.arthurdw.threedots.objects

import com.arthurdw.threedots.utils.toDate
import java.util.Date

data class NewsItem(
    val title: String,
    val snippet: String,
    val url: String,
    val imageUrl: String,
    val source: String,
    val dateStr: String,
) {
    val date: Date
        get() = dateStr.toDate()
}
