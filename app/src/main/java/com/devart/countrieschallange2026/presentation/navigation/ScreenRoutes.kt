package com.devart.countrieschallange2026.presentation.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed interface Screen {
    @Serializable
    data object CountriesList : Screen

    @Serializable
    data class CountryDetail(val countryCode: String) : Screen
}