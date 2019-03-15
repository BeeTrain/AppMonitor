package ru.chernakov.appmonitor.data.cache

import io.reactivex.Observable
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationCache @Inject
internal constructor() {
    private val EXPIRATION_TIME = (1000 * 60 * 2).toLong()

    val apps = HashSet<String>()
    private var lastUpdateTime: Long = 0

    fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            val cacheItems: ArrayList<ApplicationItem> = ArrayList()

            for (String in apps) {
                if (!String.isEmpty()) {
                    cacheItems.add(ApplicationItem(String))
                }
            }

            it.onNext(cacheItems)
            it.onComplete()
        }
    }

    fun update(applicationItems: ArrayList<ApplicationItem>) {
        apps.clear()
        for (ApplicationItem in applicationItems) {
            apps.add(ApplicationItem.toString())
        }
        setLastCacheUpdate(DateUtils.getCurrentTimeInMillis())
    }

    fun isExpired(): Boolean {
        return (DateUtils.getCurrentTimeInMillis() - getLastCacheUpdate()) > EXPIRATION_TIME || App.appPreferences.isColdStart
    }

    private fun getLastCacheUpdate(): Long {
        return lastUpdateTime
    }

    private fun setLastCacheUpdate(updateTime: Long) {
        lastUpdateTime = updateTime
    }
}