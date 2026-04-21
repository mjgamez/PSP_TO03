package com.mariajosegamez.psp_to03.ejercicio3.data

import ejercicio3.data.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.zaragoza.es/sede/"

    // lazy significa que solo se crea cuando se necesita por primera vez
    val service: RetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}


