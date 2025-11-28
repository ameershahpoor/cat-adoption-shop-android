package com.example.catsadoption_shop

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SecureApiClient {

    // KEEP YOUR ORIGINAL VALUES HERE
    private const val HOSTNAME = "api.npoint.io"                // same as before
    private const val BASE_URL = "https://api.npoint.io/..."   // same as before
    private const val PIN = "sha256/................................" // same as before

    private val certificatePinner: CertificatePinner = CertificatePinner.Builder()
        .add(HOSTNAME, PIN)
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .certificatePinner(certificatePinner)
        .build()

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val api: SecureApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        // CHANGED: use Moshi instead of Gson
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()
        .create(SecureApiService::class.java)
}
