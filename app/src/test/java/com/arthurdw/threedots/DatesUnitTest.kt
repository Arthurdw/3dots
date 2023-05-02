package com.arthurdw.threedots

import com.arthurdw.threedots.utils.DateSerializer
import com.arthurdw.threedots.utils.DateTimeSerializer
import com.arthurdw.threedots.utils.dateFormatter
import com.arthurdw.threedots.utils.dateTimeFormatter
import com.arthurdw.threedots.utils.toDate
import com.arthurdw.threedots.utils.toDateTime
import com.arthurdw.threedots.utils.toIsoDate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.serialization.json.Json
import org.junit.Test
import java.util.Date


class DatesUnitTest {
    @Test
    fun `test toDateTime() function`() {
        val dateString = "2022-05-02T14:30:00.000Z"
        val date = dateString.toDateTime()
        assertNotNull(date)
        assertEquals(dateString, dateTimeFormatter.format(date))
    }

    @Test
    fun `test toDateTime() function with null date`() {
        val dateString: String? = null
        val date = dateString?.toDateTime()
        assertNull(date)
    }

    @Test
    fun `test toDate() function`() {
        val dateString = "2022-05-02"
        val date = dateString.toDate()
        assertNotNull(date)
        if (date != null) assertEquals(dateString, dateFormatter.format(date))
        else throw IllegalArgumentException("Date is null")
    }

    @Test
    fun `test toDate() function with None`() {
        val dateString = "None"
        val date = dateString.toDate()
        assertNull(date)
    }

    @Test
    fun `test toDate() function with null date`() {
        val dateString: String? = null
        val date = dateString?.toDate()
        assertNull(date)
    }

    @Test
    fun `test toIsoDate() function`() {
        val dateString = "2022-05-02T14:30:00.000Z"
        val date = dateString.toDateTime()
        val isoDate = date.toIsoDate()
        assertEquals(dateString, isoDate)
    }

    @Test
    fun `test DateTimeSerializer serialize and deserialize`() {
        val serializer = DateTimeSerializer
        val date = Date()
        val jsonString = Json.encodeToString(serializer, date)
        val deserializedDate = Json.decodeFromString(serializer, jsonString)
        assertEquals(date, deserializedDate)
    }

    @Test
    fun `test DateSerializer serialize and deserialize with date`() {
        val serializer = DateSerializer
        val dateString = "2022-05-02"
        val date = dateString.toDate()
        val jsonString = Json.encodeToString(serializer, date)
        val deserializedDate = Json.decodeFromString(serializer, jsonString)
        assertEquals(date, deserializedDate)
    }

    @Test
    fun `test DateSerializer serialize and deserialize with null`() {
        val serializer = DateSerializer
        val jsonString = Json.encodeToString(serializer, null)
        val deserializedDate = Json.decodeFromString(serializer, jsonString)
        assertNull(deserializedDate)
    }
}