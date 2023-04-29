package com.arthurdw.threedots.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

fun String.toDateTime(): Date {
    return dateTimeFormatter.parse(this) ?: throw IllegalArgumentException("Date is null")
}

fun String.toDate(): Date? {
    if (this == "None") return null
    return dateFormatter.parse(this) ?: throw IllegalArgumentException("Date is null")
}

fun Date.toIsoDate(): String {
    return dateTimeFormatter.format(this)
}

object DateTimeSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date = decoder.decodeString().toDateTime()
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(value.toIsoDate())
}

object DateSerializer : KSerializer<Date?> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date? = decoder.decodeString().toDate()
    override fun serialize(encoder: Encoder, value: Date?) {
        if (value == null) encoder.encodeString("None")
        else encoder.encodeString(value.toIsoDate())
    }
}
