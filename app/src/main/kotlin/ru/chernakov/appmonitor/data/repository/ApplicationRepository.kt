package ru.chernakov.appmonitor.data.repository

import android.content.pm.PackageManager
import io.reactivex.Observable
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.cache.ApplicationCache
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.utils.AppUtils
import ru.chernakov.appmonitor.presentation.utils.DateUtils
import ru.chernakov.appmonitor.presentation.utils.PackageUtils


class ApplicationRepository(private val cache: ApplicationCache) {

    fun get(): Observable<List<ApplicationItem>> {
        return if (!cache.isExpired(DateUtils.getCurrentTimeInMillis())) {
            cache.getApplications()
        } else {
            getApplications()
        }
    }

    fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            val appList = ArrayList<ApplicationItem>()

            val packageManager = App.instance.packageManager
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
                            AppUtils.isSystemPackage(packageInfo),
                            PackageUtils.getSHA(packageInfo.packageName, packageManager),
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
                            AppUtils.isSystemPackage(packageInfo),
                            PackageUtils.getSHA(packageInfo.packageName, packageManager),
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

                cache.putApplications(appList, DateUtils.getCurrentTimeInMillis())
                it.onNext(appList)
                it.onComplete()
            }
        }
    }
}