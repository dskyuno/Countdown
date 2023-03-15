package com.project.timemanagerment.feature.home.data.di

import com.project.timemanagerment.base.datasource.database.AppDatabase
import com.project.timemanagerment.feature.home.data.datasource.database.dao.CountdownDao
import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInDateDao
import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInWorkDao
import com.project.timemanagerment.feature.home.data.datasource.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class HomeDataBaseModule {
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideCountdownDao(appDatabase: AppDatabase): CountdownDao {
        return appDatabase.countDownDao()
    }

    @Provides
    fun provideSignInWorkDao(appDatabase: AppDatabase): SignInWorkDao {
        return appDatabase.signInWorkDao()
    }

    @Provides
    fun provideSignInDateDao(appDatabase: AppDatabase): SignInDateDao {
        return appDatabase.signInDateDao()
    }
}
