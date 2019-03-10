package ru.chernakov.appmonitor.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.chernakov.appmonitor.presentation.utils.AppUtils


class AppPreferences(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        this.editor = sharedPreferences.edit()
    }

    var isColdStart: Boolean
        get() = sharedPreferences.getBoolean(IS_COLD_START, true)
        set(isColdStart) {
            editor.putBoolean(IS_COLD_START, isColdStart)
            editor.commit()
        }


    var customFilename: String
        get() = sharedPreferences.getString(KeyCustomFilename, "1")
        set(res) {
            editor.putString(KeyCustomFilename, res)
            editor.commit()
        }

    var sortMode: String
        get() = sharedPreferences.getString(KeySortMode, "1")
        set(res) {
            editor.putString(KeySortMode, res)
            editor.commit()
        }

    var customPath: String?
        get() = sharedPreferences.getString(KeyCustomPath, AppUtils.getDefaultAppFolder().getPath())
        set(path) {
            editor.putString(KeyCustomPath, path)
            editor.commit()
        }

    var favoriteApps: Set<String>
        get() = sharedPreferences.getStringSet(KeyFavoriteApps, HashSet())
        set(favoriteApps) {
            editor.remove(KeyFavoriteApps)
            editor.commit()
            editor.putStringSet(KeyFavoriteApps, favoriteApps)
            editor.commit()
        }

    var hiddenApps: Set<String>
        get() = sharedPreferences.getStringSet(KeyHiddenApps, HashSet())
        set(hiddenApps) {
            editor.remove(KeyHiddenApps)
            editor.commit()
            editor.putStringSet(KeyHiddenApps, hiddenApps)
            editor.commit()
        }


    fun setPrimaryColorPref(res: Int?) {
        editor.putInt(KeyPrimaryColor, res!!)
        editor.commit()
    }

    fun setFABColorPref(res: Int?) {
        editor.putInt(KeyFABColor, res!!)
        editor.commit()
    }

    companion object {
        const val IS_COLD_START = "is_cold_start"

        val KeyPrimaryColor = "prefPrimaryColor"
        val KeyFABColor = "prefFABColor"
        val KeyFABShow = "prefFABShow"
        val KeyNavigationBlack = "prefNavigationBlack"
        val KeyCustomFilename = "prefCustomFilename"
        val KeySortMode = "prefSortMode"
        val KeyIsRooted = "prefIsRooted"
        val KeyCustomPath = "prefCustomPath"

        // List
        val KeyFavoriteApps = "prefFavoriteApps"
        val KeyHiddenApps = "prefHiddenApps"
    }

}