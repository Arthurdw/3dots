package com.arthurdw.threedots

import com.arthurdw.threedots.utils.toCurrencyString
import com.arthurdw.threedots.utils.toDateString
import com.arthurdw.threedots.utils.toPercentageString
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Date

class FormattingUnitTest {
    @Test
    fun `test toCurrencyString() function`() {
        val amount = 100.50f
        val currencyString = amount.toCurrencyString()
        assertEquals("$100.50", currencyString)
    }

    @Test
    fun `test toPercentageString() function with positive float`() {
        val percentage = 5.5f
        val percentageString = percentage.toPercentageString()
        assertEquals("+5.5%", percentageString)
    }

    @Test
    fun `test toPercentageString() function with negative float`() {
        val percentage = -5.5f
        val percentageString = percentage.toPercentageString()
        assertEquals("-5.5%", percentageString)
    }

    @Test
    fun `test toDateString() function with null date`() {
        val dateString = null.toDateString()
        assertEquals("N/A", dateString)
    }

    @Test
    fun `test toDateString() function with non-null date`() {
        val date = Date(1641062400000L) // 2022-01-01
        val dateString = date.toDateString()
        assertEquals("01 Jan 2022", dateString)
    }
}