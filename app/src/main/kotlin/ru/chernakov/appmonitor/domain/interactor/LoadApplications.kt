package ru.chernakov.appmonitor.domain.interactor

import io.reactivex.Observable
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.repository.ApplicationRepository


class LoadApplications (private val applicationRepository: ApplicationRepository) :
    UseCaseObservable<List<ApplicationItem>, Void>() {

    override fun buildUseCaseObservable(params: Void?): Observable<List<ApplicationItem>> {
        return applicationRepository.getApplications()
    }
}
