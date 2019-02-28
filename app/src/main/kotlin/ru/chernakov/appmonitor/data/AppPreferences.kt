package ru.chernakov.appmonitor.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.chernakov.appmonitor.data.utils.AppUtils


class AppPreferences(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    var rootStatus: Int
        get() = sharedPreferences.getInt(KeyIsRooted, 0)
        set(rootStatus) {
            editor.putInt(KeyIsRooted, rootStatus)
            editor.commit()
        }

//    val primaryColorPref: Int
//        get() = sharedPreferences.getInt(KeyPrimaryColor, context.getResources().getColor(R.color.primary))
//
//    val fabColorPref: Int
//        get() = sharedPreferences.getInt(KeyFABColor, context.getResources().getColor(R.color.fab))

    var navigationBlackPref: Boolean?
        get() = sharedPreferences.getBoolean(KeyNavigationBlack, false)
        set(res) {
            editor.putBoolean(KeyNavigationBlack, res!!)
            editor.commit()
        }

    var fabShowPref: Boolean?
        get() = sharedPreferences.getBoolean(KeyFABShow, false)
        set(res) {
            editor.putBoolean(KeyFABShow, res!!)
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

    init {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        this.editor = sharedPreferences.edit()
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