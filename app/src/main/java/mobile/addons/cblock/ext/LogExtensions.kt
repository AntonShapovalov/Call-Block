package mobile.addons.cblock.ext

import android.util.Log
import mobile.addons.cblock.BuildConfig

/**
 * Extensions for [Log]
 */

enum class Type {
    DEBUG, ERROR
}

fun log(message: String, type: Type = Type.DEBUG) = log("LOG", message, type)

fun Any.logThis(message: String) = log(javaClass.simpleName, message)

fun Any.logSubscribe(message: String) = log(javaClass.simpleName, "$message:subscribe")

fun Any.logUnsubscribe(message: String) = log(javaClass.simpleName, "$message:un-subscribe")

private fun log(tag: String, message: String, type: Type = Type.DEBUG) {
    val prefix = "***"
    if (BuildConfig.DEBUG) {
        val prefixTag = if (!tag.startsWith(prefix)) "$prefix$tag" else tag
        when (type) {
            Type.ERROR -> Log.e(prefixTag, message)
            else -> Log.d(prefixTag, message)
        }
    }
}