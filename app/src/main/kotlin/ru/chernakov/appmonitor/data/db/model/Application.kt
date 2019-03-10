package ru.chernakov.appmonitor.data.db.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import com.raizlabs.android.dbflow.structure.BaseModel
import ru.chernakov.appmonitor.data.db.AppDB

@Table(database = AppDB::class, name = Application.TABLE_NAME)
class Application : BaseModel() {
    companion object {
        const val TABLE_NAME = "applications"
    }

    @PrimaryKey
    @Unique
    @Column(name = "name")
    var name: String? = null

    @Column(name = "apk")
    var apk: String? = null

    @Column(name = "version")
    var version: String? = null

    @Column(name = "source")
    var source: String? = null

    @Column(name = "data")
    var data: String? = null

    @Column(name = "isSystem")
    var isSystem: Boolean? = null

    @Column(name = "sha")
    var sha: String? = null

    @Column(name = "installDate")
    var installDate: Long? = null

    @Column(name = "updateDate")
    var updateDate: Long? = null

    @Column(name = "is_deleted")
    var isDeleted: Boolean? = null
}