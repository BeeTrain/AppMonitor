package ru.chernakov.appmonitor

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UIThread @Inject
internal constructor() : PostExecutionThread {

    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}
