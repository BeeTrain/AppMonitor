package ru.chernakov.appmonitor.presentation.utils

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import java.security.MessageDigest

class PackageUtils {
    companion object {

        fun getSHA(packageName: String, packageManager: PackageManager): String {
            var shaRes = ""
            val sha: List<String>
            val md: MessageDigest = MessageDigest.getInstance("SHA")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val signingInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo

                sha = if (signingInfo.hasMultipleSigners()) {
                    signingInfo.apkContentsSigners.map {
                        md.update(it.toByteArray())
                        bytesToHex(md.digest())
                    }
                } else {
                    signingInfo.signingCertificateHistory.map {
                        md.update(it.toByteArray())
                        bytesToHex(md.digest())
                    }
                }
            } else {
                val signingInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures

                sha = signingInfo.map {
                    md.update(it.toByteArray())
                    bytesToHex(md.digest())
                }
            }
            for (String in sha) {
                shaRes += sha
            }

            return shaRes
        }

        fun getPackageIcon(packageName: String?): Drawable {
            val packageManager = App.instance.packageManager
            var icon: Drawable = ResourcesUtils.getDrawable(R.drawable.ic_android)
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                if (packageInfo != null) {
                    icon = packageManager.getApplicationIcon(packageInfo.applicationInfo.packageName)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                //
            }
            return icon
        }

        private fun bytesToHex(bytes: ByteArray): String {
            val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
            val hexChars = CharArray(bytes.size * 2)
            var v: Int
            for (j in bytes.indices) {
                v = bytes[j].toInt() and 0xFF
                hexChars[j * 2] = hexArray[v.ushr(4)]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            }
            return String(hexChars)
        }
    }
}
