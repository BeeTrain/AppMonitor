package ru.chernakov.appmonitor.data.cache

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.ApplicationItem

class ApplicationCache {
    private val EXPIRATION_TIME = (1000 * 60 * 2).toLong()

    private val applicationCache = ArrayList<ApplicationItem>()
    private var lastUpdateTime: Long = 0

    fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            it.onNext(applicationCache)
            it.onComplete()
        }
    }

    fun putApplications(applicationItems: List<ApplicationItem>, updateTime: Long) {
        applicationCache.clear()
        applicationCache.addAll(applicationItems)
        lastUpdateTime = updateTime
    }

    fun isExpired(currentTime: Long): Boolean {
        return (currentTime - lastUpdateTime) > EXPIRATION_TIME
    }
}