package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.domain.repository.SettingsRepository
import javax.inject.Inject

class IsHomeGuideShownUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(key: String): Boolean {
        return settingsRepository.isHomeGuideShown(key)
    }
}
