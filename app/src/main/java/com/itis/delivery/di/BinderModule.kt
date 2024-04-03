package com.itis.delivery.di

import com.itis.delivery.data.repository.FirebaseUserRepository
import com.itis.delivery.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindUserRepositoryImpl(userRepositoryImpl: FirebaseUserRepository): UserRepository

}