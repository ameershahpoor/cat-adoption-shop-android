package com.example.catsadoption_shop


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CatRepository {
    private const val BASE_URL = "https://api.thecatapi.com/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatApiService::class.java)

    suspend fun fetchCats(): List<CatResponse> {
        return api.getCats()
    }
}
