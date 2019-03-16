package ru.chernakov.appmonitor.presentation.service

import android.app.*
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.support.v4.app.NotificationCompat
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.data.repository.EventRepository
import ru.chernakov.appmonitor.presentation.ui.AppActivity
import ru.chernakov.appmonitor.presentation.utils.AppUtils
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import java.util.*
import javax.inject.Inject


class PackageService : IntentService(TAG) {
    companion object {
        const val TAG = "PackageService"

        const val ACTION_STOP = "stopService"
        const val ACTION_START = "startService"
        const val ACTION_OPEN_APP = "runApp"

        const val SERVICE_ID = 100

        // ms
        const val TASK_LAUNCHING_PERIOD: Long = 5000
    }

    @Inject
    lateinit var applicationRepository: ApplicationRepository

    @Inject
    lateinit var eventRepository: EventRepository

    override fun onCreate() {
        App.instance.getAppComponent().inject(this)
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        val action = intent?.action

        when (action) {
            ACTION_START -> {
                startForegroundService()
            }
            ACTION_STOP -> {
                stopForegroundService()
            }
            ACTION_OPEN_APP -> {
                val notifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notifyManager.notify(SERVICE_ID, createNotification())
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            ACTION_START -> {
                startForegroundService()
            }
            ACTION_STOP -> {
                stopForegroundService()
            }
            ACTION_OPEN_APP -> {
                val notifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notifyManager.notify(SERVICE_ID, createNotification())
            }
        }

        return Service.START_NOT_STICKY
    }

    private fun startForegroundService() {
        App.isServiceRunning = true

        initTimerTask()
        startForeground(SERVICE_ID, createNotification())
    }

    private fun stopForegroundService() {
        App.isServiceRunning = false

        stopForeground(true)
        stopSelf()
    }

    private fun doWork() {
        if (App.appPreferences.isColdStart) {
            return
        }

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
                eventRepository.addEvent(eventItem)

                val text = getString(R.string.msg_package_installed, ApplicationItem.name)
                showMessageNotification(text, ApplicationItem.toString())
            }
        }

        val updatedApps = PackageUtils.getUpdatedPackages(dataPackages, checkPackages)
        if (updatedApps.size > 0) {
            for (ApplicationItem in updatedApps) {
                val eventItem = EventItem(
                    ApplicationItem.name,
                    ApplicationItem.apk,
                    ApplicationItem.version,
                    EventItem.EVENT_UPDATE,
                    ApplicationItem.updateDate,
                    PackageUtils.getPackageIcon(ApplicationItem.name)
                )
                eventRepository.addEvent(eventItem)

                val text = getString(R.string.msg_package_updated, ApplicationItem.name)
                showMessageNotification(text, ApplicationItem.toString())
            }
        }

        if (installedApps.size > 0 || updatedApps.size > 0) {
            applicationRepository.update(checkPackages)
        }
    }

    private fun initTimerTask() {
        val timer = Timer()
        val task = ShowTimer()
        timer.scheduleAtFixedRate(task, 0, TASK_LAUNCHING_PERIOD)
    }

    private fun createNotification(): Notification {
        val notifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val openAppIntent = Intent(this, AppActivity::class.java)
        openAppIntent.action = ACTION_OPEN_APP
        openAppIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val openAppPendingIntent = PendingIntent.getActivity(this, 0, openAppIntent, 0)

        val closeIntent = Intent(this, PackageService::class.java)
        closeIntent.action = ACTION_STOP
        val closePendingIntent = PendingIntent.getService(this, 0, closeIntent, 0)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val actionStopService = Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.ic_close),
                getString(R.string.title_stop),
                closePendingIntent
            ).build()

            val builder = Notification.Builder(this, TAG)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(ru.chernakov.appmonitor.R.string.app_name))
                .setContentText(getString(R.string.msg_service_running))
                .setSmallIcon(ru.chernakov.appmonitor.R.mipmap.ic_launcher)
                .setContentIntent(openAppPendingIntent)
                .addAction(actionStopService)
                .setAutoCancel(false)

            notifyManager.createNotificationChannel(
                NotificationChannel(TAG, TAG, NotificationManager.IMPORTANCE_DEFAULT)
            )

            return builder.build()
        } else {
            val actionStopService = NotificationCompat.Action.Builder(
                R.drawable.ic_close,
                getString(R.string.title_stop),
                closePendingIntent
            ).build()

            val builder = NotificationCompat.Builder(this, TAG)
                .setContentTitle(getString(ru.chernakov.appmonitor.R.string.app_name))
                .setContentText(getString(R.string.msg_service_running))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(openAppPendingIntent)
                .setWhen(System.currentTimeMillis())
                .addAction(actionStopService)
                .setAutoCancel(false)

            return builder.build()
        }
    }

    private fun showMessageNotification(text: String, applicationItemStr: String) {
        val notifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification

        val intent = Intent(this, AppActivity::class.java)
        intent.putExtra(AppUtils.INTENT_START_APP_INFO_SCREEN, applicationItemStr)
        val pIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = Notification.Builder(this, TAG)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(ru.chernakov.appmonitor.R.string.app_name))
                .setContentText(text)
                .setContentIntent(pIntent)
                .setSmallIcon(ru.chernakov.appmonitor.R.mipmap.ic_launcher)
                .setAutoCancel(true)

            notifyManager.createNotificationChannel(
                NotificationChannel(TAG, TAG, NotificationManager.IMPORTANCE_DEFAULT)
            )
            notification = builder.build()
        } else {
            val builder = NotificationCompat.Builder(this, TAG)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(ru.chernakov.appmonitor.R.string.app_name))
                .setContentText(text)
                .setContentIntent(pIntent)
                .setSmallIcon(ru.chernakov.appmonitor.R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            notification = builder.build()
        }

        notifyManager.notify(SERVICE_ID, notification)
    }

    inner class ShowTimer : TimerTask() {
        override fun run() {
            doWork()
        }
    }
}