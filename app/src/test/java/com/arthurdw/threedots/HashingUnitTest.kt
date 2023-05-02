package com.arthurdw.threedots

import com.arthurdw.threedots.utils.hashSmallString
import com.arthurdw.threedots.utils.hashString
import junit.framework.TestCase.assertEquals
import org.junit.Test

class HashingUnitTest {
    @Test
    fun `test hashString() function`() {
        val input = "Hello, world!"
        val expectedOutput = "315f5bdb76d078c43b8ac0064e4a0164612b1fce77c869345bfc94c75894edd3"
        val output = hashString(input)
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `test hashSmallString() function`() {
        val input = "Hello, world!"
        val expectedOutput = "0a9f13827b712bfc56651695b46ce0fd5eb8199c8674e4904e425b09b4c22cfc"
        val output = hashSmallString(input)
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `test hashSmallString() function with empty string`() {
        val input = ""
        val expectedOutput = "4c3316b416d469afac59d14bc82dd3866c5e5cd10b07a555fa383f2f6b0b473a"
        val output = hashSmallString(input)
        assertEquals(expectedOutput, output)
    }
}