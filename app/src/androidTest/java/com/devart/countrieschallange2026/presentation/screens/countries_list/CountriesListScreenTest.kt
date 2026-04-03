package com.devart.countrieschallange2026.presentation.screens.countries_list

import androidx.compose.ui.test.junit4.createComposeRule
import com.devart.domain.model.Country
import org.junit.Rule
import org.junit.Test

class CountriesListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockCountries = listOf(
        Country(name = "Mexico", code = "MEX", region = "Americas", population = 126000000, flagUrl = ""),
        Country(name = "Canada", code = "CAN", region = "Americas", population = 38000000, flagUrl = "")
    )

    @Test
    fun successState_displaysListOfCountries() {
        composeTestRule.setContent {
            CountriesListContent(
                countries = mockCountries,
                onCountryClick = {}
            )
        }

        val robot = CountriesListRobot(composeTestRule)

        robot.assertCountryVisible("Mexico")
        robot.assertCountryVisible("Canada")
    }

    @Test
    fun clickingCountry_triggersCallback() {
        var clickedCountryCode: String? = null

        composeTestRule.setContent {
            CountriesListContent(
                countries = mockCountries,
                onCountryClick = { clickedCountryCode = it }
            )
        }

        CountriesListRobot(composeTestRule).clickCountry("Mexico")

        assert(clickedCountryCode == "MEX")
    }
}