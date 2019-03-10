package ru.chernakov.appmonitor.data.cache

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.ApplicationItem

class ApplicationCache {
    private val EXPIRATION_TIME = (1000 * 60 * 2).toLong()

    private val applicationCache = ArrayList<String>()
    private var lastUpdateTime: Long = 0

    fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            val cacheItems: ArrayList<ApplicationItem> = ArrayList()
            for (String in applicationCache) {
                cacheItems.add(ApplicationItem(String))
            }
            it.onNext(cacheItems)
            it.onComplete()
        }
    }

    fun putApplications(applicationItems: List<ApplicationItem>, updateTime: Long) {
        applicationCache.clear()
        for (ApplicationItem in applicationItems) {
            applicationCache.add(ApplicationItem.toString())
        }
        lastUpdateTime = updateTime
    }

    fun isExpired(currentTime: Long): Boolean {
        return (currentTime - lastUpdateTime) > EXPIRATION_TIME
    }
}