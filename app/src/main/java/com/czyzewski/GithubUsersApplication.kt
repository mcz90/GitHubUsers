package com.czyzewski

import android.app.Application
import com.czyzewski.di.databaseModule
import com.czyzewski.di.networkModule
import com.czyzewski.githubuserslist.githubUsersModule
import com.czyzewski.githubuserslist.stateRecorderModule
import com.czyzewski.mvi.staterecorder.StateRecorderSetup
import com.czyzewski.userdetails.userDetailsModule
import com.facebook.stetho.Stetho
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@ObsoleteCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class GithubUsersApplication : Application() {

    @ImplicitReflectionSerializer
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
        StateRecorderSetup.init {
            enabled = true
            maxRecords = -1
            testButtonEnabled = true
            isInReplayMode = true
        }

        startKoin {
            androidContext(this@GithubUsersApplication)
            modules(
                listOf(
                    stateRecorderModule,
                    networkModule,
                    databaseModule,
                    githubUsersModule,
                    userDetailsModule
                )
            )
        }
    }
}
