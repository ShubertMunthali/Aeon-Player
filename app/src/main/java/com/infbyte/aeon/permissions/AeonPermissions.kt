package com.infbyte.aeon.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment.isExternalStorageManager
import androidx.core.content.ContextCompat

object AeonPermissions {
    fun isReadPermissionGranted(context: Context): Boolean{
        return if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
            isExternalStorageManager()
        else context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}