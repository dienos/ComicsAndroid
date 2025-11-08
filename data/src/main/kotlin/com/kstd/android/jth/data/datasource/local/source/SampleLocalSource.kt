package com.kstd.android.jth.data.datasource.local.source

import com.kstd.android.jth.data.datasource.local.db.SampleDao
import com.kstd.android.jth.data.datasource.local.entity.SampleEntity
import jakarta.inject.Inject

interface SampleLocalSource {
    suspend fun getLocalSimple(): List<SampleEntity>
}

class SampleLocalSourceImpl @Inject constructor(
    private val dao: SampleDao
) : SampleLocalSource {
    override suspend fun getLocalSimple(): List<SampleEntity> = dao.getSamples()
}