package ru.chernakov.appmonitor.presentation.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.presentation.ui.AppActivity
import java.util.*


class PackageService : IntentService(TAG) {
    companion object {
        const val TAG = "PackageService"

        const val ACTION_STOP = "stopService"
        const val ACTION_START = "startService"
        const val ACTION_RUN_APP = "runApp"

        const val SERVICE_ID = 100
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
            ACTION_RUN_APP -> {

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

    }

    private fun initTimerTask() {
        val timer = Timer()
        val task = ShowTimer()
        timer.scheduleAtFixedRate(task, 0, 1000 * 10)
    }

    private fun createNotification(): Notification {
        val notifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationView = RemoteViews(packageName, ru.chernakov.appmonitor.R.layout.layout_notification)

        val notificationIntent = Intent(this, AppActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val openAppIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val closeServiceIntent = Intent(this, PackageService::class.java)
        closeServiceIntent.action = ACTION_STOP
        notificationView.setOnClickPendingIntent(
            ru.chernakov.appmonitor.R.id.btClose, PendingIntent.getService(this, 100, closeServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = Notification.Builder(this, TAG)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(ru.chernakov.appmonitor.R.string.app_name))
                .setContentText(TAG)
                .setSmallIcon(ru.chernakov.appmonitor.R.mipmap.ic_launcher)
                .setCustomContentView(notificationView)
                .setContentIntent(openAppIntent)
                .setAutoCancel(false)

            notifyManager.createNotificationChannel(
                NotificationChannel(TAG, TAG, NotificationManager.IMPORTANCE_DEFAULT)
            )

            return builder.build()
        } else {
            val builder = NotificationCompat.Builder(this, TAG)
                .setContentTitle(getString(ru.chernakov.appmonitor.R.string.app_name))
                .setContentText(TAG)
                .setCustomContentView(notificationView)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(openAppIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)

            return builder.build()
        }
    }

    inner class ShowTimer : TimerTask() {
        override fun run() {
            doWork()
        }
    }
}