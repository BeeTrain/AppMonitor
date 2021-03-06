package ru.chernakov.appmonitor.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class AppPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    companion object {
        const val IS_COLD_START = "is_cold_start"
        const val DEVICE_APPS = "device_apps"
        const val EVENTS_HISTORY = "events_history"
    }

    init {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        this.editor = sharedPreferences.edit()
    }

    var isColdStart: Boolean
        get() = sharedPreferences.getBoolean(IS_COLD_START, true)
        set(isColdStart) {
            editor.putBoolean(IS_COLD_START, isColdStart)
            editor.commit()
        }

    var deviceApps: Set<String>
        get() = sharedPreferences.getStringSet(DEVICE_APPS, HashSet())
        set(deviceApps) {
            editor.remove(DEVICE_APPS)
            editor.commit()
            editor.putStringSet(DEVICE_APPS, deviceApps)
            editor.commit()
        }

    var eventsHistory: Set<String>
        get() = sharedPreferences.getStringSet(EVENTS_HISTORY, HashSet())
        set(eventsHistory) {
            editor.remove(EVENTS_HISTORY)
            editor.commit()
            editor.putStringSet(EVENTS_HISTORY, eventsHistory)
            editor.commit()
        }
}
