package com.project.timemanagerment.app.presentation

import android.content.Context

abstract class MyGetContext {
    companion object {

        private lateinit var context: Context

        fun setContext(con: Context) {
            context = con
        }

        fun getContext(): Context {
            return context
        }
    }
}