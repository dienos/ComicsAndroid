package com.kstd.android.jth.ui.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kstd.android.jth.domain.model.local.LocalSample
import com.kstd.android.jth.domain.model.remote.Sample
import com.kstd.android.jth.domain.usecase.GetLocalSampleUseCase
import com.kstd.android.jth.domain.usecase.GetSampleUseCase
import com.kstd.android.jth.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val getSampleUseCase: GetSampleUseCase,
    private val getLocalSampleUseCase: GetLocalSampleUseCase,
) : BaseViewModel() {

    private var _sampleData = MutableLiveData<List<Sample>>()
    val sampleRepository : LiveData<List<Sample>> = _sampleData

    private var _sampleLocalData = MutableLiveData<List<LocalSample>>()
    val sampleLocalRepository : LiveData<List<LocalSample>> = _sampleLocalData

    fun getSimpleData() {
        viewModelScope.launch {
            _sampleData.value = getSampleUseCase.invoke()
        }
    }

    fun getLocalSimpleData() {
        viewModelScope.launch {
            _sampleLocalData.value = getLocalSampleUseCase.invoke()
        }
    }
}