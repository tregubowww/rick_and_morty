package ru.tregubowww.rick_and_morty.app

import android.app.Application
import android.content.Context
import ru.tregubowww.rick_and_morty.di.AppComponent
import ru.tregubowww.rick_and_morty.di.DaggerAppComponent

class MainApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }

