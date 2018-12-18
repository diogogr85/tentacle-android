package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.data.network.ApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 15L
private const val READ_TIMEOUT = 30L

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

val networkModule = module {

    single{
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get()).build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(getProperty<String>(PROPERTY_BASE_URL))
            .client(get())
            .build()
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create<ApiService>(ApiService::class.java)
    }
}