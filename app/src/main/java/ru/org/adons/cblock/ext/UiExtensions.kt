package ru.org.adons.cblock.ext

import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.PhoneNumberUtils
import android.text.format.DateUtils
import java.util.*

/**
 * Extensions for UI elements
 *
 * Default recycler list initialization
 * @param adapter recycler adapter
 */

fun RecyclerView.initList(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    layoutManager.isSmoothScrollbarEnabled = true
    this.layoutManager = layoutManager
    this.adapter = adapter
}


@Suppress("DEPRECATION")
fun String.formatPhoneNumber(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        return PhoneNumberUtils.formatNumber(this, Locale.getDefault().country)
    } else {
        return PhoneNumberUtils.formatNumber(this)
    }
}

fun Long.getDescription(name: String?): String {
    return name?.plus(", ") ?: "" + DateUtils.getRelativeTimeSpanString(this)
}
