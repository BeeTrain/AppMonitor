package ru.chernakov.appmonitor.presentation.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.data.model.ApplicationItem
import java.io.File
import java.security.MessageDigest
import java.util.*


class PackageUtils {
    companion object {

        fun getPackages(): ArrayList<ApplicationDto> {
            val appList = HashSet<ApplicationDto>()

            val packageManager = App.instance.packageManager
            val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            for (packageInfo in packages) {
                if (!(packageManager.getApplicationLabel(packageInfo.applicationInfo) == "" || packageInfo.packageName == "")
                    && isNonSystemApp(packageInfo)
                ) {
                    try {
                        val tempApp = ApplicationDto(
                            packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                            packageInfo.packageName,
                            packageInfo.versionName,
                            packageInfo.applicationInfo.sourceDir,
                            packageInfo.applicationInfo.dataDir,
                            packageInfo.firstInstallTime,
                            packageInfo.lastUpdateTime
                        )
                        appList.add(tempApp)
                    } catch (e: OutOfMemoryError) {
                        val tempApp = ApplicationDto(
                            packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                            packageInfo.packageName,
                            packageInfo.versionName,
                            packageInfo.applicationInfo.sourceDir,
                            packageInfo.applicationInfo.dataDir,
                            packageInfo.firstInstallTime,
                            packageInfo.lastUpdateTime
                        )
                        appList.add(tempApp)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            return ArrayList(appList.toList())
        }

        fun getInstalledPackages(
            sourceData: List<ApplicationDto>,
            checkUpdate: List<ApplicationDto>
        ): ArrayList<ApplicationDto> {
            val installed = ArrayList<ApplicationDto>()

            for (fromUpdate: ApplicationDto in checkUpdate) {
                var isNew = true
                for (fromDb: ApplicationDto in sourceData) {
                    if (fromUpdate.name == fromDb.name) {
                        isNew = false
                    }
                }
                if (isNew) {
                    installed.add(fromUpdate)
                }
            }

            return installed
        }

        fun getUninstalledPackages(
            sourceData: List<ApplicationDto>,
            checkUpdate: List<ApplicationDto>
        ): ArrayList<ApplicationDto> {
            val uninstalled = ArrayList<ApplicationDto>()
            for (fromDb: ApplicationDto in sourceData) {
                var isDeleted = true
                for (fromUpdate: ApplicationDto in checkUpdate) {
                    if (fromDb.name == fromUpdate.name && fromDb.apk == fromUpdate.apk) {
                        isDeleted = false
                    }
                }
                if (isDeleted) {
                    uninstalled.add(fromDb)
                }
            }

            return uninstalled
        }

        fun getUpdatedPackages(
            sourceData: List<ApplicationDto>,
            checkUpdate: List<ApplicationDto>
        ): ArrayList<ApplicationDto> {
            val updated = ArrayList<ApplicationDto>()

            for (fromUpdate: ApplicationDto in checkUpdate) {
                for (fromDb: ApplicationDto in sourceData) {
                    if (fromUpdate.name == fromDb.name) {
                        if (fromUpdate.updateDate!! > fromDb.updateDate!!) {
                            updated.add(fromUpdate)
                        }
                    }
                }
            }

            return updated
        }

        fun getSHA(sourceFileDir: String, packageManager: PackageManager): String {
            var shaRes = ""
            val sha: List<String>
            val md: MessageDigest = MessageDigest.getInstance("SHA")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val signingInfo =
                    packageManager.getPackageArchiveInfo(
                        sourceFileDir,
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                        .signingInfo

                sha = if (signingInfo == null) {
                    listOf()
                } else {
                    if (signingInfo.hasMultipleSigners()) {
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
                }
            } else {
                val signingInfo =
                    packageManager.getPackageArchiveInfo(
                        sourceFileDir,
                        PackageManager.GET_SIGNATURES
                    ).signatures

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
            var icon: Drawable =
                ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_app)
            try {
                val packageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                if (packageInfo != null) {
                    icon =
                        packageManager.getApplicationIcon(packageInfo.applicationInfo.packageName)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                //
            }
            return icon
        }

        fun isNonSystemApp(packageInfo: PackageInfo): Boolean {
            try {
                val packageManager = App.instance.packageManager

                return packageInfo.applicationInfo.sourceDir.startsWith("/data/app/")
                        && packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null
            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }
        }

        fun getApplicationItem(packageName: String?): ApplicationItem? {
            try {
                val packageManager = App.instance.packageManager
                val packageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)

                return ApplicationItem(
                    packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                    packageInfo.packageName,
                    packageInfo.versionName,
                    packageInfo.applicationInfo.sourceDir,
                    packageInfo.applicationInfo.dataDir,
                    packageManager.getApplicationIcon(packageInfo.applicationInfo),
                    AppUtils.isSystemPackage(packageInfo),
                    PackageUtils.getSHA(packageInfo.applicationInfo.sourceDir, packageManager),
                    packageInfo.firstInstallTime,
                    packageInfo.lastUpdateTime,
                    verifyInstaller(packageName!!),
                    getFileSize(packageInfo.applicationInfo.sourceDir)
                )
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return null
            }
        }

        fun getFileSize(packageDir: String?): Long {
            val file = File(packageDir)
            if (!file.exists())
                return 0
            if (!file.isDirectory)
                return file.length()
            val dirs = LinkedList<File>()
            dirs.add(file)
            var result: Long = 0
            while (!dirs.isEmpty()) {
                val dir = dirs.removeAt(0)
                if (!dir.exists())
                    continue
                val listFiles = dir.listFiles()
                if (listFiles == null || listFiles.size == 0)
                    continue
                for (child in listFiles) {
                    result += child.length()
                    if (child.isDirectory)
                        dirs.add(child)
                }
            }
            return result
        }

        fun verifyInstaller(packageName: String): Boolean {
            val packageManager = App.instance.packageManager
            val installer = packageManager.getInstallerPackageName(packageName)

            return installer != null && AppUtils.VALID_INSTALLERS.contains(installer)
        }

        private fun bytesToHex(bytes: ByteArray): String {
            val hexArray = charArrayOf(
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'
            )
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
