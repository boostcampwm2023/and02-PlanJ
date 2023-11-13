package com.boostcamp.planj.di

import android.content.Context
import androidx.room.Room
import com.boostcamp.planj.data.db.AppDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    //retrofit

    //okhttp



    //room
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "planj"
        ).build()
    }



}