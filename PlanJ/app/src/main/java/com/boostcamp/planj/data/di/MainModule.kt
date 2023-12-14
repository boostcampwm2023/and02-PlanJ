package com.boostcamp.planj.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.BuildConfig
import com.boostcamp.planj.data.network.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {


    @Provides
    fun provideInterceptor(datastore: DataStore<Preferences>): Interceptor {
        val user = stringPreferencesKey(BuildConfig.USER)

        val dataUserFlow = datastore.data.map { pref ->
            pref[user] ?: ""
        }

        return Interceptor { chain ->
            runBlocking {
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("Authorization", dataUserFlow.first())
                    .build()
                chain.proceed(request)
            }
        }
    }

    //retrofit
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideService(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }


}