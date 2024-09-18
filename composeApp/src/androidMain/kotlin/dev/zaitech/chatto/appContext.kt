package dev.zaitech.chatto

import android.content.Context
import java.lang.ref.WeakReference


object appContext {
    private var value: WeakReference<Context?>? = null
    fun set(context: Context) {
        value = WeakReference(context)
    }
    internal fun get(): Context {
        return value?.get() ?: throw RuntimeException("Context Error")
    }
}