package ru.chernakov.appmonitor.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router


class LocalCiceroneHolder {

    private var flowRouter: Cicerone<Router>? = null

    fun getLocalRouter(): Cicerone<Router>? {
        if (flowRouter == null) {
            flowRouter = Cicerone.create()
        }
        return flowRouter
    }
}