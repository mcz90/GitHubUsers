package com.czyzewski.di

import androidx.room.Room
import com.czyzewski.database.AppDatabase
import com.czyzewski.database.LocalUsersDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "AppDatabase.db"
        ).build()
    }
    single { get<AppDatabase>().usersDao() }
    single { get<AppDatabase>().requestDataDao() }
    single { LocalUsersDataSource(get(), get()) }
}