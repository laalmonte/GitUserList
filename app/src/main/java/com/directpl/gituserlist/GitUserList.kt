package com.directpl.gituserlist

import android.app.Application
import com.directpl.gituserlist.util.SessionManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GitUserList: Application() {
    override fun onCreate() {
        super.onCreate()

        SessionManager.init(this)
    }
}