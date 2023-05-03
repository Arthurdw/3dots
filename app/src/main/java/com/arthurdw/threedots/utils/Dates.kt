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

/**
 * Formatter for date and time using the ISO-8601 format with UTC time zone.
 */
val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

/**
 * Formatter for date using the ISO-8601 format with UTC time zone.
 */
val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

/**
 * Converts a string in ISO-8601 format with UTC time zone to a [Date] object.
 *
 * @return The [Date] object representing the input date and time.
 * @throws IllegalArgumentException If the input string is null.
 */
fun String.toDateTime(): Date {
    return dateTimeFormatter.parse(this) ?: throw IllegalArgumentException("Date is null")
}

/**
 * Converts a string in ISO-8601 format with UTC time zone to a [Date] object.
 *
 * @return The [Date] object representing the input date.
 *         Returns null if the input string is "None".
 * @throws IllegalArgumentException If the input string is null.
 */
fun String.toDate(): Date? {
    if (this == "None") return null
    return dateFormatter.parse(this) ?: throw IllegalArgumentException("Date is null")
}

/**
 * Converts a [Date] object to a string in ISO-8601 format with UTC time zone.
 *
 * @return The string representation of the input date and time.
 */
fun Date.toIsoDate(): String {
    return dateTimeFormatter.format(this)
}

/**
 * Serializer for [Date] objects using the ISO-8601 format with UTC time zone.
 */
object DateTimeSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date = decoder.decodeString().toDateTime()
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(value.toIsoDate())
}

/**
 * Serializer for nullable [Date] objects using the ISO-8601 format with UTC time zone.
 */
object DateSerializer : KSerializer<Date?> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date? = decoder.decodeString().toDate()
    override fun serialize(encoder: Encoder, value: Date?) {
        if (value == null) encoder.encodeString("None")
        else encoder.encodeString(value.toIsoDate())
    }
}
