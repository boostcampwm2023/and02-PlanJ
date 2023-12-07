package com.boostcamp.planj.data.di

import com.boostcamp.planj.data.repository.ServiceRepository
import com.boostcamp.planj.data.repository.ServiceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@InstallIn(ServiceComponent::class)
@Module
abstract class WidgetRepositoryModule {

    @Binds
    abstract fun provideServiceRepository(
        serviceRepositoryImpl: ServiceRepositoryImpl
    ): ServiceRepository
}