package ru.org.adons.cblock.ext

import android.util.Log
import ru.org.adons.cblock.BuildConfig

/**
 * Extensions for [Log]
 */

enum class Type {
    debug, error
}

fun log(message: String, type: Type = Type.debug) = log("LOG", message, type)

fun Any.logThis(message: String) = log(javaClass.simpleName, message)

fun Any.logSubscribe(message: String) = log(javaClass.simpleName, "$message:subscribe")

fun Any.logUnsubscribe(message: String) = log(javaClass.simpleName, "$message:un-subscribe")

private fun log(tag: String, message: String, type: Type = Type.debug) {
    val prefix = "***"
    if (BuildConfig.DEBUG) {
        val prefixTag = if (!tag.startsWith(prefix)) "$prefix$tag" else tag
        when (type) {
            Type.error -> Log.e(prefixTag, message)
            else -> Log.d(prefixTag, message)
        }
    }
}