package ru.chernakov.appmonitor.data.cache

import io.reactivex.Observable
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.presentation.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationCache @Inject
internal constructor() {
    private val EXPIRATION_TIME = (1000 * 60 * 2).toLong()

    val apps = HashSet<String>()
    private var lastUpdateTime: Long = 0

    fun getApplications(): Observable<List<ApplicationDto>> {
        return Observable.create {
            val cacheItems: ArrayList<ApplicationDto> = ArrayList()

            for (String in apps) {
                if (!String.isEmpty()) {
                    cacheItems.add(ApplicationDto(String))
                }
            }

            it.onNext(cacheItems)
            it.onComplete()
        }
    }

    fun update(applicationItems: ArrayList<ApplicationDto>) {
        apps.clear()
        for (ApplicationDto in applicationItems) {
            apps.add(ApplicationDto.toString())
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