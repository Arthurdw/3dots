package com.arthurdw.threedots.utils

import java.security.MessageDigest

/**
 * Generates a SHA-256 hash string for a given input string with additional "3dots" and its reverse string appended to it.
 *
 * @param input The input string to hash
 * @return The hashed string
 */
fun hashSmallString(input: String): String = hashString(input + "3dots" + input.reversed())

/**
 * Generates a SHA-256 hash string for a given input string.
 *
 * @param input The input string to hash
 * @return The hashed string
 */
fun hashString(input: String): String {
    val bytes = input.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}