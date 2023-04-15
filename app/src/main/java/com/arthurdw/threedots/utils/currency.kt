package com.arthurdw.threedots.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@SuppressLint("ConstantLocale")
val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
    currency = Currency.getInstance("USD")
}

internal fun Float.toCurrencyString(): String {
    return currencyFormatter.format(this)
}

internal fun Double.toCurrencyString(): String {
    return currencyFormatter.format(this)
}
