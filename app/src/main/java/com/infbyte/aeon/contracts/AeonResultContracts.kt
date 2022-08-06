package com.infbyte.aeon.contracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import com.infbyte.aeon.permissions.AeonPermissions.isReadPermissionGranted

object  AeonResultContracts {

    class API30RequestPermission:ActivityResultContract<String, Boolean>(){

        private var context: Context? = null

        override fun createIntent(context: Context, input: String): Intent {
            val packageUri = Uri.parse(input)
            this.context = context
            return Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, packageUri)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            if(context != null)
                isReadPermissionGranted(context!!)
            context = null
            return true
        }
    }
}