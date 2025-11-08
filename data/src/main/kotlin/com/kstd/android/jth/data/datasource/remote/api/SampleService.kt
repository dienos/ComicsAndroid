package com.kstd.android.jth.data.datasource.remote.api

import com.kstd.android.jth.data.datasource.remote.dto.SampleDTO
import retrofit2.http.GET

interface SampleService {
    @GET("sample")
    suspend fun getSample() : List<SampleDTO>
}