package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.data.repository.SampleRepository
import com.kstd.android.jth.domain.model.remote.Sample

class GetSampleUseCase(private val repository: SampleRepository) {
    suspend operator fun invoke(
    ): List<Sample> {
        val result: MutableList<Sample> = mutableListOf()
        val response = repository.getSample()

        response.forEach {
            result.add(Sample(it.name))
        }

        return result
    }
}