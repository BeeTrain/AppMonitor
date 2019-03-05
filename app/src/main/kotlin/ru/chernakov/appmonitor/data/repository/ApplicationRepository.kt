package ru.chernakov.appmonitor.data.repository

import android.content.pm.PackageManager
import android.support.v4.app.FragmentActivity
import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.utils.getSHA
import ru.chernakov.appmonitor.data.utils.isSystemPackage


class ApplicationRepository(val activity: FragmentActivity) {

    fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            val appList = ArrayList<ApplicationItem>()

            val packageManager = activity.packageManager
            val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            for (packageInfo in packages) {
                if (!(packageManager.getApplicationLabel(packageInfo.applicationInfo) == "" || packageInfo.packageName == "")) {
                    try {
                        val tempApp = ApplicationItem(
                            packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                            packageInfo.packageName,
                            packageInfo.versionName,
                            packageInfo.applicationInfo.sourceDir,
                            packageInfo.applicationInfo.dataDir,
                            packageManager.getApplicationIcon(packageInfo.applicationInfo),
                            isSystemPackage(packageInfo),
                            getSHA(packageInfo.packageName, packageManager),
                            packageInfo.firstInstallTime,
                            packageInfo.lastUpdateTime
                        )
                        appList.add(tempApp)
                    } catch (e: OutOfMemoryError) {
                        val tempApp = ApplicationItem(
                            packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                            packageInfo.packageName,
                            packageInfo.versionName,
                            packageInfo.applicationInfo.sourceDir,
                            packageInfo.applicationInfo.dataDir,
                            packageManager.getApplicationIcon(packageInfo.applicationInfo),
                            isSystemPackage(packageInfo),
                            getSHA(packageInfo.packageName, packageManager),
                            packageInfo.firstInstallTime,
                            packageInfo.lastUpdateTime
                        )
                        appList.add(tempApp)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (appList.size == packages.size) {
                it.onNext(appList)
                it.onComplete()
            }
        }
    }
}