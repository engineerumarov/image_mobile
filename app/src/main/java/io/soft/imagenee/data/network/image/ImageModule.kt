package io.soft.imagenee.data.network.image

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.soft.imagenee.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {
    @Provides
    fun provideImageApi(
        client: OkHttpClient
    ): ImageApi {
        val gsonConverterFactory = GsonConverterFactory.create()
        return Retrofit
            .Builder()
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create()
    }
}