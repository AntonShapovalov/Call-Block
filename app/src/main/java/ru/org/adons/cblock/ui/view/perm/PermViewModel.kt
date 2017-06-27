package ru.org.adons.cblock.ui.view.perm

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Check all required permissions
 */

class PermViewModel {

    private val permissions = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE)

    /**
     * Check if need permissions request
     * @param activity Activity
     *
     * @return array of required permissions
     */
    fun getPermissionsRequest(activity: Activity): PermRequest {
        val request = permissions.filter { ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED }
        val isShowWarning = request.any { ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }
        return PermRequest(isShowWarning, request.toTypedArray())
    }

    /**
     * Check if all requested permissions were granted

     * @param grantResults result from onRequestPermissionsResult()
     * *
     * @return true if all permissions granted
     */
    fun isPermissionsGranted(permissions: Array<String>, grantResults: IntArray): Boolean {
        val result = grantResults.filter { it == PackageManager.PERMISSION_GRANTED } // granted=0, not=-1
        return result.size == permissions.size
    }

}

class PermRequest(val isShowWarning: Boolean, val request: Array<String>)