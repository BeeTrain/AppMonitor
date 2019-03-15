package ru.chernakov.appmonitor.domain.interactor.base.observer

import io.reactivex.observers.DisposableObserver

internal open class BaseObserver<T> : DisposableObserver<T>() {

    override fun onNext(t: T) {}

    override fun onComplete() {}

    override fun onError(e: Throwable) {}
}