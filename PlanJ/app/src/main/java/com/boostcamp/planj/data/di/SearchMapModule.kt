package com.boostcamp.planj.data.di

import com.boostcamp.planj.BuildConfig
import com.boostcamp.planj.data.network.KaKaoSearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SearchMapModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Map


    @Singleton
    @Provides
    @Map
    fun provideInterceptor() : Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API}")
                .build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    @Map
    fun provideOkHttpClient(@Map interceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    @Map
    fun provideLoginRetrofit(@Map okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(@Map retrofit: Retrofit) : KaKaoSearchApi {
        return retrofit.create(KaKaoSearchApi::class.java)
    }


}