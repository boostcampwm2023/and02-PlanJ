package com.boostcamp.planj.data.di

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.BuildConfig
import com.boostcamp.planj.data.network.MainApi
import com.boostcamp.planj.data.network.NaverApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NaverDirectionModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Naver

    @Singleton
    @Provides
    @Naver
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_API_KEY)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_CLIENT_SECRET)
                .build()
            chain.proceed(request)
        }
    }


    //retrofit
    @Singleton
    @Provides
    @Naver
    fun provideOkHttpClient(@Naver interceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Naver
    fun provideRetrofitInstance(@Naver okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideService(@Naver retrofit: Retrofit): NaverApi {
        return retrofit.create(NaverApi::class.java)
    }
}