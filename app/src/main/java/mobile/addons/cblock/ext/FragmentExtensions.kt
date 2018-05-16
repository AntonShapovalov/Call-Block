package mobile.addons.cblock.ext

import android.app.Fragment
import android.view.Gravity
import android.widget.Toast
import mobile.addons.cblock.R

/**
 * Extensions for [Fragment]
 */

fun Fragment.toastBottom(message: String) {
    val y = resources.getDimensionPixelOffset(R.dimen.toast_margin_bottom)
    val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, y)
    toast.show()
}