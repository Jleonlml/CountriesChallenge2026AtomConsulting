package com.devart.countrieschallange2026.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.devart.countrieschallange2026.presentation.screens.countries_list.CountriesListScreen
import com.devart.countrieschallange2026.presentation.screens.countries_list.CountriesListViewModel
import com.devart.countrieschallange2026.presentation.screens.country_detail.CountryDetailScreen
import com.devart.countrieschallange2026.presentation.screens.country_detail.CountryDetailViewModel

@Composable
fun CountriesNavHost(
    navController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CountriesList,
        modifier = modifier
    ) {
        composable<Screen.CountriesList> {
            val viewModel: CountriesListViewModel = viewModel(factory = viewModelFactory)

            CountriesListScreen(
                viewModel = viewModel,
                onCountryClick = { countryCode ->
                    navController.navigate(Screen.CountryDetail(countryCode))
                }
            )
        }

        composable<Screen.CountryDetail> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Screen.CountryDetail>()

            val detailViewModel: CountryDetailViewModel = viewModel(factory = viewModelFactory)

            CountryDetailScreen(
                countryCode = detailRoute.countryCode,
                viewModel = detailViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}