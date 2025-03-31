package uk.ac.tees.mad.tuneflow.model.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.tuneflow.model.serviceapi.DeezerApiService

object DeezerRetrofitInstance {
    const val BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/"
    fun create(): DeezerApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(DeezerApiService::class.java)
    }
}