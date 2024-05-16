package com.itis.delivery.di

import com.itis.delivery.data.repository.CartRepositoryImpl
import com.itis.delivery.data.repository.CategoryRepositoryImpl
import com.itis.delivery.data.repository.ProductRepositoryImpl
import com.itis.delivery.data.repository.RateRepositoryImpl
import com.itis.delivery.data.repository.UserRepositoryImpl
import com.itis.delivery.domain.repository.CartRepository
import com.itis.delivery.domain.repository.CategoryRepository
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.domain.repository.RateRepository
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
    fun bindUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl): UserRepository


    @Binds
    @Singleton
    fun bindProductRepositoryImpl(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    fun bindCategoryRepositoryImpl(categoryRepositoryImpl: CategoryRepositoryImpl) : CategoryRepository

    @Binds
    @Singleton
    fun bindCartRepositoryImpl(cartRepositoryImpl: CartRepositoryImpl) : CartRepository

    @Binds
    @Singleton
    fun bindRateRepositoryImpl(rateRepositoryImpl: RateRepositoryImpl) : RateRepository
}