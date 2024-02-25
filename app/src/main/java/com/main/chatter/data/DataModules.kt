package com.main.chatter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.main.chatter.NotificationHandler
import com.main.chatter.database.AppDatabase
import com.main.chatter.database.MessageDAO
import com.main.chatter.database.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

// Based on How to build a data layer by Android Developers https://www.youtube.com/watch?v=P125nWICYps

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, "AppDatabase"
        ).build()
    }

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDAO = appDatabase.messageDao()

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDAO = appDatabase.userDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun provideNotificationHandler(@ApplicationContext context: Context): NotificationHandler {
        return NotificationHandler(context)
    }
}

// https://medium.com/androiddevelopers/datastore-and-dependency-injection-ea32b95704e3
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler {
            emptyPreferences()
        },
            migrations = listOf(SharedPreferencesMigration(context, "userLogin")),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("userLogin") })
    }
}