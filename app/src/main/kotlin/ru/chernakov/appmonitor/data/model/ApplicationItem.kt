package ru.chernakov.appmonitor.data.model

import android.graphics.drawable.Drawable
import java.io.Serializable


class ApplicationItem : Serializable {

    var name: String? = null
        private set

    var apk: String? = null
        private set

    var version: String? = null
        private set

    var source: String? = null
        private set

    var data: String? = null
        private set
    var icon: Drawable? = null

    var isSystem: Boolean? = null
        private set

    var sha: String? = null
        private set

    var installDate: Long? = null
        private set

    var updateDate: Long? = null
        private set

    constructor(
        name: String,
        apk: String,
        version: String,
        source: String,
        data: String,
        icon: Drawable,
        isSystem: Boolean?,
        sha: String?,
        installDate: Long,
        updateDate: Long
    ) {
        this.name = name
        this.apk = apk
        this.version = version
        this.source = source
        this.data = data
        this.icon = icon
        this.isSystem = isSystem
        this.sha = sha
        this.installDate = installDate
        this.updateDate = updateDate
    }

    constructor(string: String) {
        val split = string.split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size == 9) {
            this.name = split[0]
            this.apk = split[1]
            this.version = split[2]
            this.source = split[3]
            this.data = split[4]
            this.isSystem = java.lang.Boolean.getBoolean(split[5])
            this.sha = split[6]
            this.installDate = split[7].toLong()
            this.updateDate = split[8].toLong()
        }
    }

    override fun toString(): String {
        return "$name##$apk##$version##$source##$data##$isSystem##$sha##$installDate##$updateDate"
    }
}
