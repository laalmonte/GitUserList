package com.directpl.gituserlist.util

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.securepreferences.SecurePreferences

object SessionManager {
    private const val KEY_LAST_ID             = "UserSessionManager.KEY_LAST_ID"

    private lateinit var sharedPref: SecurePreferences

    public val _triggerRefresh = MutableLiveData<Boolean>()
    val triggerRefresh: LiveData<Boolean>
        get() = _triggerRefresh

    fun init(context: Context) {
        sharedPref = SecurePreferences(context)
    }

    var lastId: Int
        get() = sharedPref.getInt(KEY_LAST_ID, 0) ?: 0
        set(value) {
            sharedPref.edit { it.putInt(KEY_LAST_ID, value) }
        }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun clearData() {
        sharedPref.edit().clear()
    }

}