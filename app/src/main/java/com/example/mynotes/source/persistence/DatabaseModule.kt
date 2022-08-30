package com.example.mynotes.source.persistence

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideNoteClient(get()) }
}

fun provideNoteClient(databaseClient: DatabaseClient) = databaseClient.noteDao

fun provideDatabase(application: Application) = Room.databaseBuilder(
    application,
    DatabaseClient::class.java,
    "noteDatabase.db",
).fallbackToDestructiveMigration().build()