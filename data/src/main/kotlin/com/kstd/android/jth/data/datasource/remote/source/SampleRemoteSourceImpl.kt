package com.kstd.android.jth.data.datasource.remote.source

import com.kstd.android.jth.data.datasource.remote.api.SampleService
import com.kstd.android.jth.data.datasource.remote.dto.SampleDTO
import javax.inject.Inject

interface SampleRemoteSource {
    suspend fun getSimple(): List<SampleDTO>
}

class SampleRemoteSourceImpl @Inject constructor(
    private val sampleService: SampleService
) : SampleRemoteSource {
    override suspend fun getSimple(): List<SampleDTO> = sampleService.getSample()
}