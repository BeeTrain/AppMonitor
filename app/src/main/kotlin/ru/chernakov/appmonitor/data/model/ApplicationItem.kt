package ru.chernakov.appmonitor.data.model

import android.graphics.drawable.Drawable
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.io.Serializable


@Table(name = "applications")
class ApplicationItem : Serializable {

    @Column(name = "name")
    var name: String? = null
        private set

    @Column(name = "apk")
    var apk: String? = null
        private set

    @Column(name = "version")
    var version: String? = null
        private set

    @Column(name = "source")
    var source: String? = null
        private set

    @Column(name = "data")
    var data: String? = null
        private set
    @Column(name = "icon")
    var icon: Drawable? = null

    @Column(name = "is_system")
    var isSystem: Boolean? = null
        private set

    constructor()

    constructor(
        name: String,
        apk: String,
        version: String,
        source: String,
        data: String,
        icon: Drawable,
        isSystem: Boolean?
    ) {
        this.name = name
        this.apk = apk
        this.version = version
        this.source = source
        this.data = data
        this.icon = icon
        this.isSystem = isSystem
    }

    constructor(string: String) {
        val split = string.split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size == 6) {
            this.name = split[0]
            this.apk = split[1]
            this.version = split[2]
            this.source = split[3]
            this.data = split[4]
            this.isSystem = java.lang.Boolean.getBoolean(split[5])
        }
    }

    override fun toString(): String {
        return "$name##$apk##$version##$source##$data##$isSystem"
    }
}
