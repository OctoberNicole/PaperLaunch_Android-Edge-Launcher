package de.devmil.paperlaunch.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import de.devmil.paperlaunch.R
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi


object PermissionUtils {
    val REQUEST_DRAW_OVERLAY_PERMISSION = 10001

    fun checkOverlayPermission(context : Context) : Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        return Settings.canDrawOverlays(context)
    }

    fun checkOverlayPermissionAndRouteToSettingsIfNeeded(activity : Activity) : Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if(!checkOverlayPermission(activity)) {
            var ad = AlertDialog.Builder(activity)
            ad.setTitle(R.string.permission_request_title)
            ad.setMessage(R.string.permission_request_description)
            ad.setNeutralButton(R.string.permission_request_button_ok) { _, _ ->
                launchOverlaySettings(activity)
            }
            ad.show()
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun launchOverlaySettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.packageName))
        activity.startActivityForResult(intent, REQUEST_DRAW_OVERLAY_PERMISSION)
    }
}