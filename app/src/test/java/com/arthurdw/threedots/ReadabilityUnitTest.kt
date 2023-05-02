package com.arthurdw.threedots

import com.arthurdw.threedots.utils.empty
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ReadabilityUnitTest {
    @Test
    fun `test String empty() function`() {
        val expectedOutput = ""
        val output = String.empty()
        assertEquals(expectedOutput, output)
    }
}