package ru.chernakov.appmonitor.data.repository

import android.content.pm.PackageManager
import android.support.v4.app.FragmentActivity
import io.reactivex.Observable
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem

class ApplicationRepository(val activity: FragmentActivity) {

    fun getApplications(): Observable<ArrayList<ApplicationItem>> {
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
                        false
                    )
                    appList.add(tempApp)
                } catch (e: OutOfMemoryError) {
                    val tempApp = ApplicationItem(
                        packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                        packageInfo.packageName,
                        packageInfo.versionName,
                        packageInfo.applicationInfo.sourceDir,
                        packageInfo.applicationInfo.dataDir,
                        activity.resources.getDrawable(R.drawable.ic_android),
                        false
                    )
                    appList.add(tempApp)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return Observable.just(appList)
    }
}