package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.data.repository.SampleRepository
import com.kstd.android.jth.domain.model.local.LocalSample

class GetLocalSampleUseCase(private val repository: SampleRepository) {
    suspend operator fun invoke(
    ): List<LocalSample> {
        val result: MutableList<LocalSample> = mutableListOf()
        val response = repository.getLocalSample()

        response.forEach {
            result.add(LocalSample(it.id, it.name))
        }

        return result
    }
}