package com.devart.countrieschallange2026.presentation.screens.country_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devart.countrieschallange2026.presentation.common.UiState
import com.devart.domain.model.Country
import com.devart.domain.useCase.GetCountryByCodeUseCase
import com.devart.domain.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class CountryDetailViewModel @Inject constructor(
    private val getCountryByCodeUseCase: GetCountryByCodeUseCase
) : ViewModel() {

    private val _countryCode = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<Country>> = _countryCode
        .filterNotNull()
        .flatMapLatest { code ->
            getCountryByCodeUseCase(code).map { resource ->
                when (resource) {
                    is Resource.Success -> UiState.Success(resource.data)
                    is Resource.Error -> UiState.Error(resource.message)
                    is Resource.Loading -> UiState.Loading
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun getCountryDetail(code: String) {
        if (_countryCode.value != code) {
            _countryCode.value = code
        }
    }

    fun retry() {
        val currentCode = _countryCode.value
        _countryCode.value = null
        _countryCode.value = currentCode
    }
}