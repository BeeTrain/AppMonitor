package ru.chernakov.appmonitor.presentation.service

import android.app.*
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.support.v4.app.NotificationCompat
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.presentation.ui.AppActivity
import ru.chernakov.appmonitor.presentation.utils.AppUtils
import ru.chernakov.appmonitor.presentation.utils.DateUtils
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
    }

    @Inject
    lateinit var applicationRepository: ApplicationRepository

    override fun onCreate() {
        App.instance.getAppComponent().inject(this)
        super.onCreate()

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

    override fun onHandleIntent(intent: Intent?) {
        val action = intent?.action

        when (action) {
            ACTION_START -> {
                startForegroundService()
            }
            ACTION_STOP -> {
                stopForegroundService()
            }
        }
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
        if (!App.appPreferences.isColdStart) {
            val dataPackages = applicationRepository.getFromPrefs()
            val checkPackages = PackageUtils.getPackages()

            val installedApps =
                PackageUtils.getInstalledPackages(
                    dataPackages,
                    checkPackages
                )
            if (installedApps.size > 0) {
                for (ApplicationItem in installedApps) {
                    val text = getString(R.string.msg_package_installed, ApplicationItem.name)
                    showMessageNotification(text, ApplicationItem.toString())
                }
            }

            val updatedApps =
                PackageUtils.getUpdatedPackages(
                    dataPackages,
                    checkPackages
                )
            if (updatedApps.size > 0) {
                for (ApplicationItem in updatedApps) {
                    val text = getString(R.string.msg_package_updated, ApplicationItem.name)
                    showMessageNotification(text, ApplicationItem.toString())
                }
            }

//            val uninstalledApps =
//                PackageUtils.getUninstalledPackages(
//                    dataPackages,
//                    checkPackages
//                )
//            if (uninstalledApps.size > 0) {
//                for (ApplicationItem in uninstalledApps) {
//                    val text = ApplicationItem.name + " был удален"
//                    showMessageNotification(text)
//                }
//            }
            if (installedApps.size > 0 || updatedApps.size > 0) {
                applicationRepository.saveToPrefs(checkPackages)
            }
        } else {
            if (applicationRepository.cache.applicationCache.size > 0
                && applicationRepository.cache.isExpired(DateUtils.getCurrentTimeInMillis())
            )
                applicationRepository.saveToPrefs(PackageUtils.getPackages())
        }
    }

    private fun initTimerTask() {
        val timer = Timer()
        val task = ShowTimer()
        timer.scheduleAtFixedRate(task, 0, 30000)
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

    fun showMessageNotification(text: String, applicationItemStr: String) {
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