package com.directpl.gituserlist.module

import com.directpl.gituserlist.api.UserService
import com.directpl.gituserlist.db.dao.UsersDao
import com.directpl.gituserlist.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTrackRepository(
        usersDao: UsersDao,
        userService: UserService
    ): UserRepository =
        UserRepository(usersDao, userService)

}