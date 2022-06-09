package com.android.architecture.core

import com.android.architecture.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS

object RemoteClientBuilder {

    private var retrofit: Retrofit? = null
    var baseUrl: HttpUrl? = null

    private val restHttpClient by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor {
                    Timber.tag("OkHttp").d(it)
                }
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            }
            readTimeout(5, SECONDS)
            writeTimeout(5, SECONDS)
        }.build()
    }

    fun buildRetrofit(): Retrofit {
        retrofit.let { cached ->
            if (cached == null || cached.baseUrl() != baseUrl) {
                return newRetrofit(baseUrl).also { retrofit = it }
            }
            return cached
        }
    }

    private fun newRetrofit(baseUrl: HttpUrl?): Retrofit {
        val moshiConvert = MoshiConverterFactory.create(
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        )
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(moshiConvert)
            .client(restHttpClient)

        if (baseUrl != null) {
            builder.baseUrl(baseUrl)
        }

        return builder.build()
    }
}
