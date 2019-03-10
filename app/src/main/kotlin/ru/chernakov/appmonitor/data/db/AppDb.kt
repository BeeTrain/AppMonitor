package ru.chernakov.appmonitor.data.db

import com.raizlabs.android.dbflow.annotation.Database
import ru.chernakov.appmonitor.BuildConfig

@Database(name = AppDB.DB_NAME, version = AppDB.VERSION)
class AppDB {

    companion object {
        const val DB_NAME = "application_db"

        const val VERSION = BuildConfig.VERSION_CODE
    }

}