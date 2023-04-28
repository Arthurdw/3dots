package com.arthurdw.threedots.data.objects

import com.arthurdw.threedots.utils.DateSerializer
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
    @Serializable(with = DateSerializer::class)
    val date: Date,
)

