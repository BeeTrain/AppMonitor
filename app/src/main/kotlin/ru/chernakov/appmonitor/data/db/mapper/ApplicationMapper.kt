package ru.chernakov.appmonitor.data.db.mapper

import ru.chernakov.appmonitor.data.db.model.Application
import ru.chernakov.appmonitor.data.model.ApplicationItem

class ApplicationMapper {
    companion object {

        fun transform(applicationItems: List<ApplicationItem>): ArrayList<Application> {
            val transformed: ArrayList<Application> = ArrayList()

            for (ApplicationItem in applicationItems) {
                transformed.add(transform(ApplicationItem))
            }

            return transformed
        }

        fun transform(applicationItem: ApplicationItem): Application {
            val transformed = Application()
            transformed.name = applicationItem.name
            transformed.apk = applicationItem.apk
            transformed.data = applicationItem.data
            transformed.source = applicationItem.source
            transformed.sha = applicationItem.sha
            transformed.installDate = applicationItem.installDate
            transformed.updateDate = applicationItem.updateDate
            transformed.isSystem = applicationItem.isSystem
            transformed.isDeleted = false

            return transformed
        }
    }
}