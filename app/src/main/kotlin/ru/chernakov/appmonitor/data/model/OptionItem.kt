package ru.chernakov.appmonitor.data.model

import android.graphics.drawable.Drawable

class OptionItem(id: Int, title: String, icon: Drawable) {

    var id: Int? = id
        private set

    var title: String? = title
        private set

    var icon: Drawable? = icon
        private set
}
