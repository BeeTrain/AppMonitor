package ru.chernakov.appmonitor.data.db.model

import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import ru.chernakov.appmonitor.data.db.AppDB

@Table(database = AppDB::class, name = History.TABLE_NAME)
class History : BaseModel() {
    companion object {
        const val TABLE_NAME = "history"

        const val ACTION_INIT = "init"

        const val ACTION_INSTALL = "install"

        const val ACTION_UPDATE = "update"

        const val ACTION_UNINSTALL = "uninstall"
    }

    var id: Int? = null

    var action: String? = null

    var name: String? = null

    var date: Long? = null
}
