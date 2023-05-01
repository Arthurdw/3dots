package com.arthurdw.threedots.utils

import java.security.MessageDigest

fun hashSmallString(input: String): String = hashString(input + "3dots" + input.reversed())

fun hashString(input: String): String {
    val bytes = input.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}