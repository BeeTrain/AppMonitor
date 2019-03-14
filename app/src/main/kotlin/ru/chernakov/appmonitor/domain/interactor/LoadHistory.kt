package ru.chernakov.appmonitor.domain.interactor

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.data.repository.HistoryRepository
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor

class LoadHistory(
    private val historyRepository: HistoryRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    UseCaseObservable<List<EventItem>, Void>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<List<EventItem>> {
        return historyRepository.get()
    }
}
