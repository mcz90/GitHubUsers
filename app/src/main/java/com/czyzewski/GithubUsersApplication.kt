package com.czyzewski

import android.app.Application
import com.czyzewski.di.databaseModule
import com.czyzewski.di.networkModule
import com.czyzewski.githubuserslist.githubUsersModule
import com.facebook.stetho.Stetho
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@InternalCoroutinesApi
class GithubUsersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
        startKoin {
            androidContext(this@GithubUsersApplication)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    githubUsersModule
                )
            )
        }
    }
}
