package com.project.timemanagerment.feature.profile.data.di


import com.project.timemanagerment.feature.profile.data.datasource.network.api.UserServer
import com.project.timemanagerment.feature.profile.data.datasource.network.api.VipPriceServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileNetModule {
    @Singleton
    @Provides
    fun provideUserServer(): UserServer {
        return UserServer.create();
    }

    @Singleton
    @Provides
    fun provideVipPriceServer(): VipPriceServer {
        return VipPriceServer.create()
    }

}