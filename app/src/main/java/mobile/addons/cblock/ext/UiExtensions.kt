package mobile.addons.cblock.ext

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
fun String.formatPhoneNumber(): String = try {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        PhoneNumberUtils.formatNumber(this, Locale.getDefault().country)
    } else {
        PhoneNumberUtils.formatNumber(this)
    }
} catch (e: Exception) {
    e.printStackTrace()
    "UNKNOWN"
}

fun Long.getDescription(name: String?): String {
    val date = DateUtils.getRelativeTimeSpanString(this)
    return name?.plus(", $date") ?: date.toString()
}

