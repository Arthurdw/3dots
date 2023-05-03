package com.arthurdw.threedots.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

/**
 * Number format instance for formatting currency values with the US dollar currency.
 */
@SuppressLint("ConstantLocale")
val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
    currency = Currency.getInstance("USD")
}

/**
 * Converts a float to a currency string using [currencyFormatter].
 *
 * @return The currency string representation of the float.
 */
internal fun Float.toCurrencyString(): String {
    return currencyFormatter.format(this)
}

/**
 * Converts a float to a percentage string.
 *
 * @return The percentage string representation of the float.
 */
internal fun Float.toPercentageString(): String {
    val prefix = if (this > 0) "+" else ""
    return "$prefix${this}%"
}

/**
 * Converts a [Date] to a date string in the format "dd MMM yyyy" using the device's default locale.
 *
 * @return The date string representation of the [Date].
 */
internal fun Date?.toDateString(): String {
    if (this == null) return "N/A"

    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(this)
}