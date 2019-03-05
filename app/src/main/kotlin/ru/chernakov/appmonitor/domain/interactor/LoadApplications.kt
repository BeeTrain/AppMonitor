package ru.chernakov.appmonitor.domain.interactor

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor


class LoadApplications(
    private val applicationRepository: ApplicationRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    UseCaseObservable<List<ApplicationItem>, Void>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<List<ApplicationItem>> {
        return applicationRepository.getApplications()
    }
}
