package ru.chernakov.appmonitor.domain.interactor

import io.reactivex.Single
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.domain.interactor.base.usecase.UseCaseSingle
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanPackages @Inject
internal constructor(
    private val applicationRepository: ApplicationRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCaseSingle<List<EventItem>, Void>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseSingle(params: Void?): Single<List<EventItem>> {
        return Single.create {
            if (App.appPreferences.isColdStart) {
                it.onSuccess(ArrayList())
            }
            try {
                val events = scan()
                it.onSuccess(events)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    fun scan(): ArrayList<EventItem> {
        val events = ArrayList<EventItem>()

        if (!App.appPreferences.isColdStart) {
            val dataPackages = applicationRepository.getFromPrefs()
            val checkPackages = PackageUtils.getPackages()

            val installedApps = PackageUtils.getInstalledPackages(dataPackages, checkPackages)
            if (installedApps.size > 0) {
                for (ApplicationItem in installedApps) {
                    val eventItem = EventItem(
                        ApplicationItem.name,
                        ApplicationItem.apk,
                        ApplicationItem.version,
                        EventItem.EVENT_INSTALL,
                        ApplicationItem.updateDate,
                        PackageUtils.getPackageIcon(ApplicationItem.name)
                    )

                    events.add(eventItem)
                }
            }

            val updatedApps = PackageUtils.getUpdatedPackages(dataPackages, checkPackages)
            if (updatedApps.size > 0) {
                for (ApplicationItem in updatedApps) {
                    events.add(
                        EventItem(
                            ApplicationItem.name,
                            ApplicationItem.apk,
                            ApplicationItem.version,
                            EventItem.EVENT_UPDATE,
                            ApplicationItem.updateDate,
                            PackageUtils.getPackageIcon(ApplicationItem.name)
                        )
                    )
                }
            }

            val uninstalledApps = PackageUtils.getUninstalledPackages(dataPackages, checkPackages)
            if (uninstalledApps.size > 0) {
                for (ApplicationItem in updatedApps) {
                    events.add(
                        EventItem(
                            ApplicationItem.name,
                            ApplicationItem.apk,
                            ApplicationItem.version,
                            EventItem.EVENT_UNINSTALL,
                            ApplicationItem.updateDate,
                            PackageUtils.getPackageIcon(ApplicationItem.name)
                        )
                    )
                }
            }

            if (installedApps.size > 0 || updatedApps.size > 0 || uninstalledApps.size > 0) {
                applicationRepository.update(checkPackages)
            }
        } else {
            if (applicationRepository.cache.apps.size > 0
                && applicationRepository.cache.isExpired()
            )
                applicationRepository.update(PackageUtils.getPackages())
        }

        return events
    }
}