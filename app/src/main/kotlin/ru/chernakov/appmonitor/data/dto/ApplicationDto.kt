package ru.chernakov.appmonitor.data.dto

import java.io.Serializable

class ApplicationDto : Serializable {

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

    var installDate: Long? = null
        private set

    var updateDate: Long? = null
        private set


    constructor(string: String) {
        val split = string.split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size == 7) {
            this.name = split[0]
            this.apk = split[1]
            this.version = split[2]
            this.source = split[3]
            this.data = split[4]
            this.installDate = split[5].toLong()
            this.updateDate = split[6].toLong()
        }
    }

    constructor(
        name: String?,
        apk: String?,
        version: String?,
        source: String?,
        data: String?,
        installDate: Long?,
        updateDate: Long?
    ) {
        this.name = name
        this.apk = apk
        this.version = version
        this.source = source
        this.data = data
        this.installDate = installDate
        this.updateDate = updateDate
    }

    override fun toString(): String {
        return "$name##$apk##$version##$source##$data##$installDate##$updateDate"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationDto

        if (name != other.name) return false
        if (apk != other.apk) return false
        if (version != other.version) return false
        if (source != other.source) return false
        if (data != other.data) return false
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
        result = 31 * result + (installDate?.hashCode() ?: 0)
        result = 31 * result + (updateDate?.hashCode() ?: 0)
        return result
    }
}