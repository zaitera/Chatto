package dev.zaitech.chatto

import android.content.Context

actual fun getAppContext(): Context = appContext.get()