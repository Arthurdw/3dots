package com.arthurdw.threedots.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

@SuppressLint("ConstantLocale")
val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
    currency = Currency.getInstance("USD")
}

internal fun Float.toCurrencyString(): String {
    return currencyFormatter.format(this)
}

internal fun Float.toPercentageString(): String {
    val prefix = if (this > 0) "+" else ""
    return "$prefix${this}%"
}

internal fun Date?.toDateString(): String {
    if (this == null) return "N/A"

    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(this)
}