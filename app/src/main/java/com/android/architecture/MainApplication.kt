package com.android.architecture

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MainApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}
