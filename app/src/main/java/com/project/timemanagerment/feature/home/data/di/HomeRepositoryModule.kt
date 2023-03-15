package com.project.timemanagerment.feature.home.data.di

import com.project.timemanagerment.feature.home.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {
  /*  @Binds
    abstract fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository*/

    @Binds
    abstract fun provideCountdownRepository(countdownRepositoryImpl: CountdownRepositoryImpl): CountdownRepository

    @Binds
    abstract fun provideSignInWorkRepository(signInWorkRepositoryImpl: SignInWorkRepositoryImpl): SignInWorkRepository

    @Binds
    abstract fun provideSignInDateRepository(sinInDateRepositoryImpl: SignInDateRepositoryImpl): SignInDateRepository
}