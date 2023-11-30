package com.boostcamp.planj.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.boostcamp.planj.BuildConfig
import com.boostcamp.planj.data.db.AlarmInfoDao
import com.boostcamp.planj.data.db.AppDatabase
import com.boostcamp.planj.data.db.CategoryDao
import com.boostcamp.planj.data.db.ScheduleDao
import com.boostcamp.planj.data.db.UserDao
import com.boostcamp.planj.data.network.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Singleton
    @Provides
    fun provideInterceptor(datastore: DataStore<Preferences>): Interceptor {
        val user = stringPreferencesKey(BuildConfig.USER)

        val dataUserFlow = datastore.data.map { pref ->
            pref[user] ?: ""
        }

        val userToken = runBlocking {
            dataUserFlow.first()
        }

        return Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Authorization", userToken)
                .build()
            chain.proceed(request)
        }
    }

    //retrofit
    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }

    //okhttp

    //room
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideAlarmInfoDao(appDatabase: AppDatabase): AlarmInfoDao = appDatabase.alarmInfoDao()

    @Singleton
    @Provides
    fun provideScheduleDao(appDatabase: AppDatabase): ScheduleDao = appDatabase.scheduleDao()

    @Singleton
    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao = appDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    //DataStore
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(BuildConfig.DATA_STORE_NAME) }
        )

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}