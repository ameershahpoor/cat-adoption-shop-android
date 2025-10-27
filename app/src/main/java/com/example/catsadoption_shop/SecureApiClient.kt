package com.example.catsadoption_shop


import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SecureApiClient {
    // my npoint host and bin
    private const val HOSTNAME = "api.npoint.io"
    // BASE_URL ends always with a slash
    private const val BASE_URL = "https://api.npoint.io/b90955cbda6a6c5bc66e/"

    // The certificate public-key pin
    private const val PIN = "sha256/0Xa7UdRqkzcnNrkuBuWltp9rt1S7FS3KZ7z4JQ8YdRU="

    private val certificatePinner: CertificatePinner = CertificatePinner.Builder()
        .add(HOSTNAME, PIN)
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .certificatePinner(certificatePinner)
        .build()

    val api: SecureApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SecureApiService::class.java)
}
