package ru.chernakov.appmonitor.data.db.mapper

import ru.chernakov.appmonitor.data.db.model.Application
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.utils.PackageUtils

class ApplicationMapper {
    companion object {

        fun transform(applications: List<Application>): ArrayList<ApplicationItem> {
            val transformed: ArrayList<ApplicationItem> = ArrayList()

            for (Application in applications) {
                transformed.add(transform(Application))
            }

            return transformed
        }

        fun transform(application: Application): ApplicationItem {
            val transformed = ApplicationItem(
                application.name!!,
                application.apk!!,
                application.version!!,
                application.source!!,
                application.data!!,
                PackageUtils.getPackageIcon(application.apk),
                application.isSystem,
                application.sha,
                application.installDate!!,
                application.updateDate!!
            )

            return transformed
        }
    }
}