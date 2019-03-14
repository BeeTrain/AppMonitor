package ru.chernakov.appmonitor.data.model

import android.graphics.drawable.Drawable
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import java.io.Serializable

class EventItem : Serializable {

    companion object {
        const val EVENT_INSTALL = "install"

        const val EVENT_UPDATE = "update"

        const val EVENT_UNINSTALL = "uninstall"
    }

    var name: String? = null
        private set

    var apk: String? = null
        private set

    var version: String? = null
        private set

    var action: String? = null
        private set

    var actionDate: Long? = null
        private set

    var icon: Drawable? = null
        set

    constructor(name: String?, apk: String?, version: String?, action: String?, actionDate: Long?, icon: Drawable) {
        this.name = name
        this.apk = apk
        this.version = version
        this.action = action
        this.actionDate = actionDate
        this.icon = icon
    }

    constructor(string: String) {
        val split = string.split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size == 5) {
            this.name = split[0]
            this.apk = split[1]
            this.version = split[2]
            this.action = split[3]
            this.actionDate = split[4].toLong()

            this.icon = PackageUtils.getPackageIcon(this.apk)
        }
    }

    override fun toString(): String {
        return "$name##$apk##$version##$action##$actionDate"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventItem

        if (name != other.name) return false
        if (apk != other.apk) return false
        if (version != other.version) return false
        if (action != other.action) return false
        if (actionDate != other.actionDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (apk?.hashCode() ?: 0)
        result = 31 * result + (version?.hashCode() ?: 0)
        result = 31 * result + (action?.hashCode() ?: 0)
        result = 31 * result + (actionDate?.hashCode() ?: 0)
        return result
    }
}
