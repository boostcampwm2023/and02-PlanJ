package com.boostcamp.planj.data.di

import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.LoginRepositoryImpl
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import com.boostcamp.planj.data.repository.NaverRepository
import com.boostcamp.planj.data.repository.NaverRepositoryImpl
import com.boostcamp.planj.data.repository.SearchRepository
import com.boostcamp.planj.data.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    abstract fun provideMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    @Binds
    abstract fun provideSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun provideNaverRepository(
        naverRepository: NaverRepositoryImpl
    ): NaverRepository
}