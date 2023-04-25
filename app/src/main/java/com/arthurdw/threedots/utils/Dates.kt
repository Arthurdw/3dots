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

val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

fun String.toDate(): Date {
    return formatter.parse(this) ?: throw IllegalArgumentException("Date is null")
}

fun Date.toIsoDate(): String {
    return formatter.format(this)
}

object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date = decoder.decodeString().toDate()
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(value.toIsoDate())
}
