package com.arthurdw.threedots.data.objects

import com.arthurdw.threedots.utils.DateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NewsItem(
    val title: String,
    val snippet: String,
    val url: String,
    @SerialName("image_url")
    val imageUrl: String,
    val source: String,
    @SerialName("published_at")
    @Serializable(with = DateTimeSerializer::class)
    val date: Date,
)

