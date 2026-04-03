package com.devart.countrieschallange2026.presentation.screens.country_detail

import app.cash.turbine.test
import com.devart.countrieschallange2026.presentation.common.UiState
import com.devart.countrieschallange2026.util.MainDispatcherRule
import com.devart.domain.model.Country
import com.devart.domain.useCase.GetCountryByCodeUseCase
import com.devart.domain.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCountryByCodeUseCase = mockk<GetCountryByCodeUseCase>()
    private lateinit var viewModel: CountryDetailViewModel

    private val mockCountry = Country(
        name = "Mexico",
        officialName = "United Mexican States",
        flagUrl = "url_png",
        population = 126000000,
        code = "MEX",
        region = "Americas",
        capital = "Mexico City",
        subRegion = "North America",
        languages = listOf("Spanish"),
        currencies = listOf("Mexican Peso"),
        timezones = listOf("UTC-06:00")
    )

    @Before
    fun setup() {
        viewModel = CountryDetailViewModel(getCountryByCodeUseCase)
    }

    @Test
    fun `when getCountryDetail is called, uiState should transition from Loading to Success`() = runTest {
        // Given
        val code = "MEX"
        coEvery { getCountryByCodeUseCase(code) } returns flowOf(
            Resource.Loading,
            Resource.Success(mockCountry)
        )

        // Then

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Loading)

            viewModel.getCountryDetail(code)

            val finalState = awaitItem()
            assertThat(finalState).isInstanceOf(UiState.Success::class.java)

            val successData = (finalState as UiState.Success).data
            assertThat(successData.name).isEqualTo("Mexico")
            assertThat(successData.code).isEqualTo("MEX")
        }
    }

    @Test
    fun `when UseCase returns Error, uiState should emit Error state`() = runTest {
        // Given
        val code = "XYZ"
        val errorMsg = "Country not found"
        coEvery { getCountryByCodeUseCase(code) } returns flowOf(Resource.Error(errorMsg))

        // When/Then
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Loading)

            viewModel.getCountryDetail(code)

            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(UiState.Error::class.java)
            assertThat((errorState as UiState.Error).message).isEqualTo(errorMsg)
        }
    }

    @Test
    fun `when retry is called, it should trigger the UseCase again`() = runTest {
        // Given
        val code = "MEX"
        coEvery { getCountryByCodeUseCase(code) } returns flowOf(Resource.Error("Initial Failure"))

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Loading) // Initial state from stateIn

            viewModel.getCountryDetail(code)
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(UiState.Error::class.java)

            coEvery { getCountryByCodeUseCase(code) } returns flowOf(
                Resource.Loading,
                Resource.Success(mockCountry)
            )

            // When
            viewModel.retry()

            // Then
            val result = awaitItem()
            assertThat(result).isInstanceOf(UiState.Success::class.java)
            assertThat((result as UiState.Success).data.code).isEqualTo("MEX")
        }
    }

    @Test
    fun `getCountryDetail with same code should not trigger new emissions`() = runTest {
        // Given
        val code = "MEX"
        coEvery { getCountryByCodeUseCase(code) } returns flowOf(Resource.Success(mockCountry))

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Loading)

            viewModel.getCountryDetail(code)
            awaitItem()

            viewModel.getCountryDetail(code)

            // Then
            expectNoEvents()
        }
    }
}