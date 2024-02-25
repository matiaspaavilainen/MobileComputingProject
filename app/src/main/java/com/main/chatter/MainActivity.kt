package com.main.chatter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.main.chatter.data.AppRepository
import com.main.chatter.database.AppDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var notificationHandler: NotificationHandler

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationHandler.notificationPermission(requestPermissionLauncher, this)

        notificationHandler.createNotificationChannel("all")

        installSplashScreen()
        setContent {
            MyNavHost()
        }
    }
}