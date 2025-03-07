package com.example.kharido.Internet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/GeekyShaswat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api by lazy {
        retrofit.create(ApiInterface::class.java)
    }
    val api2 by lazy {
        retrofit.create(Api2::class.java)
    }
    val api3 by lazy{
        retrofit.create(Api3::class.java)
    }

}