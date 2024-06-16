package com.itis.delivery.di

import android.annotation.SuppressLint
import com.itis.delivery.BuildConfig
import com.itis.delivery.base.Constants
import com.itis.delivery.base.Keys
import com.itis.delivery.data.remote.OpenFoodFactsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val clientBuilder = if (BuildConfig.DEBUG) {
            createUnsafeClient()
        } else {
            OkHttpClient.Builder()
        }

        insertBaseQueryParameters(clientBuilder)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return clientBuilder.build()
    }

    private fun insertBaseQueryParameters(clientBuilder: OkHttpClient.Builder) {
        clientBuilder.addInterceptor { chain ->
            val newUrl = chain.request().url.newBuilder()
                .addQueryParameter(Keys.JSON_KEY, Constants.JSON_VALUE.toString())
                .addQueryParameter(
                    Keys.SEARCH_SIMPLE_KEY,
                    Constants.SEARCH_SIMPLE_VALUE.toString()
                )
                .addEncodedQueryParameter(Keys.FIELDS_KEY, Constants.FIELDS_VALUE)
                .addQueryParameter(Keys.PAGE_SIZE_KEY, Constants.PAGE_SIZE_VALUE.toString())
                .addQueryParameter(Keys.SEARCH_TERM_KEY, Constants.EMPTY_STRING)
                .build()

            val requestBuilder = chain.request().newBuilder().url(newUrl)

            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideOpenFoodFactsApi(okHttpClient: OkHttpClient): OpenFoodFactsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OPEN_FOOD_FACTS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }


    private fun createUnsafeClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            okHttpClient
        } catch (e: Exception) {
            okHttpClient
        }
    }
}