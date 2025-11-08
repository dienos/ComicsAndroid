package com.kstd.android.jth.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.kstd.android.jth.data.datasource.remote.api.ImageApi
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    private const val CONNECT_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L
    private const val BASE_URL = "https://openapi.naver.com/"

    private const val NAVER_CLIENT_ID = "qyH6HEUSPGfPLfb47n_x"
    private const val NAVER_API_KEY = "4hfvRd2tp0"

    class NaverAuthInterceptor() : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", NAVER_API_KEY)
                .build()

            return chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(NaverAuthInterceptor())
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesService(retrofit: Retrofit): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }
}