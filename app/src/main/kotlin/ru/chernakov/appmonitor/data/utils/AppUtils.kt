package ru.chernakov.appmonitor.data.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import org.apache.commons.io.FileUtils
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem
import java.io.File
import java.io.IOException


class AppUtils {


    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_READ = 1

        fun getDefaultAppFolder(): File {
            return File(Environment.getExternalStorageDirectory().path + File.separator + App.instance.getString(R.string.app_name))
        }

        fun getAppFolder(): File {
            val appFolder = getDefaultAppFolder()
            if (!appFolder.exists()) {
                appFolder.mkdirs()
            }
            return appFolder
        }

        fun copyFile(applicationItem: ApplicationItem): Boolean? {
            var result: Boolean? = false

            val initialFile = File(applicationItem.source)
            val finalFile = getOutputFilename(applicationItem)

            try {
                FileUtils.copyFile(initialFile, finalFile)
                result = true
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return result
        }

        fun getOutputFilename(applicationItem: ApplicationItem): File {
            return File(getAppFolder().path + "/" + getAPKFilename(applicationItem) + ".apk")
        }

        fun getAPKFilename(applicationItem: ApplicationItem): String {
            val appPreferences = App.appPreferences
            val res: String

            when (appPreferences.customFilename) {
                "1" -> res = applicationItem.apk + "_" + applicationItem.version
                "2" -> res = applicationItem.name + "_" + applicationItem.version
                "4" -> res = applicationItem.name.toString()
                else -> res = applicationItem.apk.toString()
            }

            return res
        }

        fun checkPermissions(activity: Activity): Boolean? {
            var res: Boolean? = false
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), MY_PERMISSIONS_REQUEST_WRITE_READ
                )
            } else {
                res = true
            }
            return res
        }
    }
}