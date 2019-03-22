package ru.chernakov.appmonitor.domain.interactor

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.domain.interactor.base.usecase.UseCaseObservable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadApplications @Inject
constructor(
    private val applicationRepository: ApplicationRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    UseCaseObservable<List<ApplicationDto>, Void>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<List<ApplicationDto>> {
        return applicationRepository.get()
    }
}
