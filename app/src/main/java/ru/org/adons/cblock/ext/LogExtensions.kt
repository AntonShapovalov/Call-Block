package ru.org.adons.cblock.ext

import android.util.Log
import ru.org.adons.cblock.BuildConfig

/**
 * Extensions for [Log]
 */

fun log(message: String) {
    log("LOG", message)
}

fun Any.logSubscribe(message: String) {
    log(javaClass.simpleName, "$message:subscribe")
}

fun Any.logUnsubscribe(message: String) {
    log(javaClass.simpleName, "$message:un-subscribe")
}

private fun log(tag: String, message: String) {
    val prefix = "***"
    if (BuildConfig.DEBUG) {
        val prefixTag = if (!tag.startsWith(prefix)) "$prefix$tag" else tag
        Log.d(prefixTag, message)
    }
}