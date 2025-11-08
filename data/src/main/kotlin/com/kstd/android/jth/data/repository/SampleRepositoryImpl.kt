package com.kstd.android.jth.data.repository

import com.kstd.android.jth.data.datasource.local.source.SampleLocalSource
import com.kstd.android.jth.data.datasource.remote.source.SampleRemoteSource
import com.kstd.android.jth.data.datasource.local.entity.SampleEntity
import com.kstd.android.jth.data.datasource.remote.dto.SampleDTO
import jakarta.inject.Inject

interface SampleRepository {
    suspend fun getLocalSample(): List<SampleEntity>
    suspend fun getSample(): List<SampleDTO>
}

class SampleRepositoryImpl @Inject constructor(
    private val localSource: SampleLocalSource,
    private val remoteSource: SampleRemoteSource
) : SampleRepository {
    override suspend fun getLocalSample(): List<SampleEntity> = localSource.getLocalSimple()
    override suspend fun getSample(): List<SampleDTO> = remoteSource.getSimple()
}