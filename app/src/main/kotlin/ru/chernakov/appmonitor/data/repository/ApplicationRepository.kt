package ru.chernakov.appmonitor.data.repository

import io.reactivex.Observable
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.cache.ApplicationCache
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.utils.PackageUtils


class ApplicationRepository(val cache: ApplicationCache) {

    fun get(): Observable<List<ApplicationItem>> {
        return if (!cache.isExpired()) {
            cache.getApplications()
        } else {
            getApplications()
        }
    }

    private fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            val appList: ArrayList<ApplicationItem>
            if (App.appPreferences.isColdStart) {
                appList = PackageUtils.getPackages()

                update(appList)
            } else {
                appList = getFromPrefs()
            }

            it.onNext(appList)
            it.onComplete()
        }
    }

    fun update(applicationItems: ArrayList<ApplicationItem>) {
        val appsListSet = HashSet<String>()
        for (ApplicationItem in applicationItems) {
            appsListSet.add(ApplicationItem.toString())
        }
        App.appPreferences.deviceApps = appsListSet
        cache.update(applicationItems)

        App.appPreferences.isColdStart = false
    }

    fun getFromPrefs(): ArrayList<ApplicationItem> {
        val appList = ArrayList<ApplicationItem>()
        val appsListSet = App.appPreferences.deviceApps

        for (String in appsListSet) {
            appList.add(ApplicationItem(String))
        }

        return appList
    }
}