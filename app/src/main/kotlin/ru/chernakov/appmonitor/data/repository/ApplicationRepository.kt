package ru.chernakov.appmonitor.data.repository

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.cache.ApplicationCache
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.utils.DateUtils
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import java.util.*


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
            val appList = PackageUtils.getPackages()

            appList.sortWith(Comparator { p1, p2 ->
                p1.name.toString().toLowerCase()
                    .compareTo(p2.name.toString().toLowerCase())
            })

            cache.putApplications(appList, DateUtils.getCurrentTimeInMillis())
            it.onNext(appList)
            it.onComplete()
        }
    }
}