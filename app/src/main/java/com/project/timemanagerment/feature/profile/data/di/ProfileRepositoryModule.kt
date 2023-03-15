package com.project.timemanagerment.feature.profile.data.di

import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import com.project.timemanagerment.feature.profile.data.respository.UserRepositoryImpl
import com.project.timemanagerment.feature.profile.data.respository.VipPriceRepository
import com.project.timemanagerment.feature.profile.data.respository.VipPriceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {
    @Binds
    abstract fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun provideVipPriceRepository(vipPriceRepositoryImpl: VipPriceRepositoryImpl): VipPriceRepository
}