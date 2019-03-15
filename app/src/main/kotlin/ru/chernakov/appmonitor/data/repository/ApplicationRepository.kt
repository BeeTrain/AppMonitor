package ru.chernakov.appmonitor.data.repository

import io.reactivex.Observable
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.cache.ApplicationCache
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepository @Inject
internal constructor(val cache: ApplicationCache) {

    fun get(): Observable<List<ApplicationDto>> {
        return if (!cache.isExpired()) {
            cache.getApplications()
        } else {
            getApplications()
        }
    }

    private fun getApplications(): Observable<List<ApplicationDto>> {
        return Observable.create {
            val appList: ArrayList<ApplicationDto>
            if (App.appPreferences.isColdStart) {
                appList = PackageUtils.getPackages()

            } else {
                appList = getFromPrefs()
            }
            update(appList)

            it.onNext(appList)
            it.onComplete()
        }
    }

    fun update(applicationItems: ArrayList<ApplicationDto>) {
        val appsListSet = HashSet<String>()
        for (ApplicationItem in applicationItems) {
            appsListSet.add(ApplicationItem.toString())
        }
        App.appPreferences.deviceApps = appsListSet
        cache.update(applicationItems)

        App.appPreferences.isColdStart = false
    }

    fun getFromPrefs(): ArrayList<ApplicationDto> {
        val appList = ArrayList<ApplicationDto>()
        val appsListSet = App.appPreferences.deviceApps

        for (String in appsListSet) {
            appList.add(ApplicationDto(String))
        }

        return appList
    }
}