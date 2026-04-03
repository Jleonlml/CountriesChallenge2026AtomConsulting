package com.devart.countrieschallange2026.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devart.countrieschallange2026.presentation.screens.countries_list.CountriesListViewModel
import com.devart.countrieschallange2026.presentation.screens.country_detail.CountryDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CountriesListViewModel::class)
    abstract fun bindCountriesListViewModel(viewModel: CountriesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountryDetailViewModel::class)
    abstract fun bindCountryDetailViewModel(viewModel: CountryDetailViewModel): ViewModel
}