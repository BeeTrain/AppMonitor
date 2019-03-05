package ru.chernakov.appmonitor.data.utils

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import ru.chernakov.appmonitor.App

class ResourcesUtils {
    companion object {
        fun getString(resourceId: Int): String {
            return App.instance.getString(resourceId)
        }

        fun getDrawable(@DrawableRes drawableResId: Int): Drawable {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                App.instance.resources.getDrawable(drawableResId, App.instance.theme)
            } else {
                App.instance.resources.getDrawable(drawableResId)
            }
        }
    }
}
