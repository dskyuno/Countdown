package com.project.timemanagerment.feature.profile.data.datasource.network.api

import com.google.gson.GsonBuilder
import com.project.timemanagerment.app.presentation.MyGetContext
import com.project.timemanagerment.base.constant.Constants
import com.project.timemanagerment.base.constant.ConstantsPreferencesKey
import com.project.timemanagerment.base.datestore.DataStorePreferencesRepository
import com.project.timemanagerment.feature.profile.data.datasource.network.annotation.DisableToken
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetVipPriceResponse
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface VipPriceServer {
    @Headers("Content-Type: application/json")
    @POST("vi/vipPrices")
    @DisableToken
    suspend fun getVipPrice(): NetVipPriceResponse

    companion object {
        fun create(): VipPriceServer {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val interceptor = UserServer.ServiceInterceptor()
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

            return Retrofit.Builder()
                .baseUrl(Constants.SERVER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(VipPriceServer::class.java)
        }
    }


    class ServiceInterceptor : Interceptor {
        @EntryPoint
        @InstallIn(SingletonComponent::class)
        interface ServiceInterceptorEntryPoint {
            fun dataStorePreferencesRepository(): DataStorePreferencesRepository
        }


        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val annotation = request.tag(Invocation::class.java)?.method()
                ?.getAnnotation(DisableToken::class.java)
            if (annotation != null) {
                val appContext =
                    MyGetContext.getContext().applicationContext ?: throw IllegalStateException()
                val hiltEntryPoint =
                    EntryPointAccessors.fromApplication(
                        appContext,
                        ServiceInterceptorEntryPoint::class.java
                    )
                val userToken =
                    hiltEntryPoint.dataStorePreferencesRepository().getStringPreferencesSync(
                        ConstantsPreferencesKey.userToken,
                        ConstantsPreferencesKey.userToken_default_value
                    )
                //val token = getTokenFromSharedPreference();
                //or use Token Function
                val token = userToken
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                    .addHeader("Authorization", finalToken)
                    .build()
            }
            return chain.proceed(request)
        }

    }
}