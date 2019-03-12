package ru.chernakov.appmonitor.data.model

import android.graphics.drawable.Drawable
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
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
        set

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

            this.icon = PackageUtils.getPackageIcon(this.apk)
        }
    }

    override fun toString(): String {
        return "$name##$apk##$version##$source##$data##$isSystem##$sha##$installDate##$updateDate"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationItem

        if (name != other.name) return false
        if (apk != other.apk) return false
        if (version != other.version) return false
        if (source != other.source) return false
        if (data != other.data) return false
        if (icon != other.icon) return false
        if (isSystem != other.isSystem) return false
        if (sha != other.sha) return false
        if (installDate != other.installDate) return false
        if (updateDate != other.updateDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (apk?.hashCode() ?: 0)
        result = 31 * result + (version?.hashCode() ?: 0)
        result = 31 * result + (source?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (isSystem?.hashCode() ?: 0)
        result = 31 * result + (sha?.hashCode() ?: 0)
        result = 31 * result + (installDate?.hashCode() ?: 0)
        result = 31 * result + (updateDate?.hashCode() ?: 0)
        return result
    }


}
