package ru.chernakov.appmonitor.domain.interactor

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.data.repository.EventRepository
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.domain.interactor.base.usecase.UseCaseObservable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadHistory @Inject
constructor(
    private val eventRepository: EventRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    UseCaseObservable<List<EventItem>, Void>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<List<EventItem>> {
        return eventRepository.get()
    }
}
