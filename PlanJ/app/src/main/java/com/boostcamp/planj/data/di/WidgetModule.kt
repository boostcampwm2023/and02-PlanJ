package com.boostcamp.planj.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.BuildConfig
import com.boostcamp.planj.data.network.MainApi
import com.boostcamp.planj.data.network.ServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
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


@Module
@InstallIn(ServiceComponent::class)
object WidgetModule {


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Service

    @Provides
    @Service
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
    @Provides
    @Service
    fun provideOkHttpClient(@Service interceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Service
    fun provideRetrofitInstance(@Service okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideService(@Service retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }
}