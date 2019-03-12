package ru.chernakov.appmonitor.data.cache

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.ApplicationItem

class ApplicationCache {
    private val EXPIRATION_TIME = (1000 * 60 * 2).toLong()

    val applicationCache = ArrayList<String>()
    private var lastUpdateTime: Long = 0

    fun getApplications(): Observable<List<ApplicationItem>> {
        return Observable.create {
            val cacheItems: ArrayList<ApplicationItem> = ArrayList()
            val hashSet = HashSet<ApplicationItem>()

            for (String in applicationCache) {
                if (String != null && !String.isEmpty()) {
                    val item = ApplicationItem(String)
                    hashSet.add(item)
                }
            }
            cacheItems.addAll(hashSet)

            cacheItems.sortWith(Comparator { p1, p2 ->
                p1.name.toString().toLowerCase()
                    .compareTo(p2.name.toString().toLowerCase())
            })

            it.onNext(cacheItems)
            it.onComplete()
        }
    }

    fun putApplications(applicationItems: ArrayList<ApplicationItem>, updateTime: Long) {
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