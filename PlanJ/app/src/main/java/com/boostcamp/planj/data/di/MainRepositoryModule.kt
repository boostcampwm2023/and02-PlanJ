package com.boostcamp.planj.data.di

import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import com.boostcamp.planj.data.repository.NaverRepository
import com.boostcamp.planj.data.repository.NaverRepositoryImpl
import com.boostcamp.planj.data.repository.SearchRepository
import com.boostcamp.planj.data.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class MainRepositoryModule {
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