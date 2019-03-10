package ru.chernakov.appmonitor.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import org.apache.commons.io.FileUtils
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.model.ApplicationItem
import java.io.File
import java.io.IOException


class AppUtils {
    companion object {
        const val MY_PERMISSIONS_REQUEST_WRITE_READ = 1

        const val UNINSTALL_REQUEST_CODE = 1

        fun getDefaultAppFolder(): File {
            return File(Environment.getExternalStorageDirectory().path + File.separator + App.instance.getString(ru.chernakov.appmonitor.R.string.app_name))
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

        fun goToGooglePlay(context: Context, id: String) {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$id")))
            } catch (e: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$id")
                    )
                )
            }
        }

        fun getShareIntent(file: File): Intent {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            intent.type = "application/vnd.android.package-archive"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

            return intent
        }

        fun isSystemPackage(packageInfo: PackageInfo): Boolean {
            return packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
    }
}