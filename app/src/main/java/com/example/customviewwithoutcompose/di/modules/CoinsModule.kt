package com.example.customviewwithoutcompose.di.modules

import com.example.customviewwithoutcompose.data.repositories.CoinsRepositoryFake
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoinsModule {

    @Binds
    abstract fun provideCoinsRepository(coinsRepositoryImpl: CoinsRepositoryFake): CoinsRepository
}