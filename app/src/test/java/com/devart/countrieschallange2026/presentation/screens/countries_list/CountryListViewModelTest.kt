package com.devart.countrieschallange2026.presentation.screens.countries_list

import app.cash.turbine.test
import com.devart.countrieschallange2026.presentation.common.UiState
import com.devart.countrieschallange2026.util.MainDispatcherRule
import com.devart.domain.model.Country
import com.devart.domain.useCase.GetCountriesUseCase
import com.devart.domain.useCase.SearchCountriesUseCase
import com.devart.domain.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCountriesUseCase = mockk<GetCountriesUseCase>()
    private val searchCountriesUseCase = mockk<SearchCountriesUseCase>()

    private lateinit var viewModel: CountriesListViewModel

    private val mockCountries = listOf(
        Country(name = "Mexico", code = "MEX", flagUrl = "", population = 126000000, region = "Americas", capital = "Mexico City", subRegion = "North America", languages = emptyList(), currencies = emptyList(), timezones = emptyList()),
        Country(name = "Canada", code = "CAN", flagUrl = "", population = 38000000, region = "Americas", capital = "Ottawa", subRegion = "North America", languages = emptyList(), currencies = emptyList(), timezones = emptyList())
    )

    @Before
    fun setup() {
        every { searchCountriesUseCase("", any()) } returns mockCountries
    }

    @Test
    fun `initially emits Loading and then Success when data is fetched`() = runTest {
        // Given
        coEvery { getCountriesUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Success(mockCountries)
        )

        // When
        viewModel = CountriesListViewModel(getCountriesUseCase, searchCountriesUseCase)

        // Then
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Loading)

            val successState = awaitItem()
            assertThat(successState).isInstanceOf(UiState.Success::class.java)
            assertThat((successState as UiState.Success).data).isEqualTo(mockCountries)
        }
    }

    @Test
    fun `emits Error state when UseCase fails`() = runTest {
        // Given
        val errorMsg = "No Internet Connection"
        coEvery { getCountriesUseCase() } returns flowOf(Resource.Error(errorMsg))

        // When
        viewModel = CountriesListViewModel(getCountriesUseCase, searchCountriesUseCase)

        // Then
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Loading)

            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(UiState.Error::class.java)
            assertThat((errorState as UiState.Error).message).isEqualTo(errorMsg)
        }
    }

    @Test
    fun `search query updates results after debounce period`() = runTest {
        // Given
        coEvery { getCountriesUseCase() } returns flowOf(Resource.Success(mockCountries))
        val filteredList = listOf(mockCountries[0]) // Just Mexico
        every { searchCountriesUseCase("Mex", any()) } returns filteredList

        viewModel = CountriesListViewModel(getCountriesUseCase, searchCountriesUseCase)

        viewModel.uiState.test {
            skipItems(2)

            // When
            viewModel.onSearchQueryChanged("Mex")

            advanceTimeBy(301)

            //Then
            val result = awaitItem()
            assertThat(result).isInstanceOf(UiState.Success::class.java)
            assertThat((result as UiState.Success).data).containsExactly(mockCountries[0])
        }
    }

    @Test
    fun `emits Empty state when search results are empty`() = runTest {
        // Given
        coEvery { getCountriesUseCase() } returns flowOf(Resource.Success(mockCountries))
        every { searchCountriesUseCase("Unknown", any()) } returns emptyList()

        // When
        viewModel = CountriesListViewModel(getCountriesUseCase, searchCountriesUseCase)

        // Then
        viewModel.uiState.test {
            skipItems(2)

            viewModel.onSearchQueryChanged("Unknown")
            advanceTimeBy(301)

            assertThat(awaitItem()).isEqualTo(UiState.Empty)
        }
    }
}