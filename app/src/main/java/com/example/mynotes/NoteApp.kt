package com.example.mynotes

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.mynotes.source.note.noteRepositoryModule
import com.example.mynotes.source.persistence.databaseModule
import com.example.mynotes.ui.form.formModule
import com.example.mynotes.ui.form.formViewModel
import com.example.mynotes.ui.home.homeModule
import com.example.mynotes.ui.home.homeViewModel
import timber.log.Timber
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NoteApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger()
            androidContext(this@NoteApp)
            modules(
                databaseModule,
                homeModule,
                noteRepositoryModule,
                homeViewModel,
                formModule,
                formViewModel,
            )
        }
    }
}