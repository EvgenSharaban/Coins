package com.example.customviewwithoutcompose.di.modules

import com.example.customviewwithoutcompose.data.repositories.CoinsRepositoryImpl
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoinsModule {

    @Binds
    abstract fun provideCoinsRepository(coinsRepositoryImpl: CoinsRepositoryImpl): CoinsRepository
}