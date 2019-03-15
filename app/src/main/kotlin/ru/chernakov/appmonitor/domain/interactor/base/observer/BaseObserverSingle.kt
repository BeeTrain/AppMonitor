package ru.chernakov.appmonitor.domain.interactor.base.observer

import io.reactivex.observers.DisposableSingleObserver

internal open class BaseObserverSingle<T> : DisposableSingleObserver<T>() {

    override fun onSuccess(t: T) {}

    override fun onError(e: Throwable) {}
}