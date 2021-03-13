package com.directpl.gituserlist.module

import android.content.Context
import androidx.room.Room
import com.directpl.gituserlist.db.AppDb
import com.directpl.gituserlist.db.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.directpl.gituserlist.BuildConfig

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDb =
        buildDatabase(appContext)

    @Provides
    fun provideMovieDao(appDatabase: AppDb): UsersDao = appDatabase.trackDao()


    private fun buildDatabase(appContext: Context): AppDb {
        val builder = Room.databaseBuilder(
            appContext.applicationContext,
            AppDb::class.java, BuildConfig.DB_NAME
        )
        builder.allowMainThreadQueries()
        return builder.build()
    }
}