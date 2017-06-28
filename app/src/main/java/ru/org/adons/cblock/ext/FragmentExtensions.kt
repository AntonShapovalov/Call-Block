package ru.org.adons.cblock.ext

import android.app.Fragment
import android.view.Gravity
import android.widget.Toast
import ru.org.adons.cblock.R

/**
 * Extensions for [Fragment]
 */

fun Fragment.toastBottom(message: String) {
    val y = resources.getDimensionPixelOffset(R.dimen.toast_margin_bottom)
    val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, y)
    toast.show()
}