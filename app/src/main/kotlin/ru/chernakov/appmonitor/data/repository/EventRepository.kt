package ru.chernakov.appmonitor.data.repository

import io.reactivex.Observable
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.model.EventItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject
internal constructor(){

    fun get(): Observable<List<EventItem>> {
        return Observable.create {
            val eventsList = getHistoryFromPrefs()

            it.onNext(eventsList)
            it.onComplete()
        }
    }

    private fun getHistoryFromPrefs(): ArrayList<EventItem> {
        val eventsList = ArrayList<EventItem>()
        val eventsListSet = App.appPreferences.eventsHistory

        for (String in eventsListSet) {
            eventsList.add(EventItem(String))
        }

        return eventsList
    }

    fun addEvent(eventItem: EventItem) {
        val eventsListSet = HashSet<String>()
        val prefSet = App.appPreferences.eventsHistory
        eventsListSet.addAll(prefSet)
        eventsListSet.add(eventItem.toString())
        App.appPreferences.eventsHistory = eventsListSet
    }
}
