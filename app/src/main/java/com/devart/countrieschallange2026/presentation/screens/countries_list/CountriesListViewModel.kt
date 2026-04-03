package com.devart.countrieschallange2026.presentation.screens.countries_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devart.countrieschallange2026.presentation.common.UiState
import com.devart.domain.model.Country
import com.devart.domain.useCase.GetCountriesUseCase
import com.devart.domain.useCase.SearchCountriesUseCase
import com.devart.domain.util.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountriesListViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {

    private val _allCountries = MutableStateFlow<List<Country>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _loadStatus = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    @OptIn(FlowPreview::class)
    val uiState: StateFlow<UiState<List<Country>>> = combine(
        _allCountries,
        _searchQuery.debounce(300),
        _loadStatus
    ) { countries, query, status ->
        when (status) {
            is UiState.Loading -> UiState.Loading
            is UiState.Error -> UiState.Error(status.message)
            else -> {
                val filtered = searchCountriesUseCase(query, countries)

                if (filtered.isEmpty()) {
                    if (countries.isEmpty()) UiState.Empty else UiState.Empty
                } else {
                    UiState.Success(filtered)
                }
            }
        }
    }.onStart {
        if (_allCountries.value.isEmpty()) loadCountries()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun loadCountries() {
        viewModelScope.launch {
            getCountriesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _loadStatus.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _allCountries.value = resource.data
                        _loadStatus.value = UiState.Success(Unit)
                    }
                    is Resource.Error -> {
                        _loadStatus.value = UiState.Error(resource.message)
                    }
                }
            }
        }
    }
}