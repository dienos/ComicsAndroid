package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.domain.repository.SettingsRepository
import javax.inject.Inject

class SetBookMarkGuideShownUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(key: String, value : Boolean) {
        settingsRepository.setBookMarkGuideShown(key, value)
    }
}
