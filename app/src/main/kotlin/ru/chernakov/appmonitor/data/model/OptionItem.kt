package ru.chernakov.appmonitor.data.model

import android.graphics.drawable.Drawable

class OptionItem(id: Int, title: String, icon: Drawable) {
    companion object {
        const val OPEN_APP_ID = 1000

        const val SAVE_APK_ID = 1100

        const val APK_SAVED_ID = 1101

        const val PLAY_MARKET_ID =1200

        const val DELETE_APP_ID = 1300
    }

    var id: Int? = id
        private set

    var title: String? = title
        private set

    var icon: Drawable? = icon
        private set
}
