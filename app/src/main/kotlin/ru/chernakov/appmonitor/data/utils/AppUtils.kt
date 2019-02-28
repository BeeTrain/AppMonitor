package ru.chernakov.appmonitor.data.utils

import android.os.Environment
import org.apache.commons.io.FileUtils
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem
import java.io.File
import java.io.IOException


class AppUtils {

    companion object {
        fun getDefaultAppFolder(): File {
            return File(Environment.getExternalStorageDirectory().path + File.pathSeparator + R.string.app_name)
        }


        fun getAppFolder(): File {
            val appPreferences = App.appPreferences
            return File(appPreferences.customPath)
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
    }
}