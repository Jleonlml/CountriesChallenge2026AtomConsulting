package com.devart.countrieschallange2026.presentation.screens.country_detail

import androidx.compose.ui.test.junit4.createComposeRule
import com.devart.domain.model.Country
import org.junit.Rule
import org.junit.Test

class CountryDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockCountry = Country(
        name = "Mexico",
        officialName = "United Mexican States",
        code = "MEX",
        flagUrl = "https://example.com/flag.png",
        population = 126000000,
        region = "Americas",
        capital = "Mexico City",
        subRegion = "North America",
        languages = listOf("Spanish"),
        currencies = listOf("Mexican Peso"),
        timezones = listOf("UTC-06:00")
    )

    @Test
    fun successState_displaysAllDetails() {
        composeTestRule.setContent {
            CountryDetailContent(country = mockCountry)
        }

        val robot = CountryDetailRobot(composeTestRule)

        robot.assertCountryName("Mexico")
        robot.assertDetail("Official Name", "United Mexican States")
        robot.assertDetail("Capital", "Mexico City")
        robot.assertDetail("Population", "126,000,000")
        robot.assertDetail("Languages", "Spanish")
    }

    @Test
    fun errorState_displaysMessageAndRetryButton() {
        val errorMessage = "Connection failed"
        var retryClicked = false

        composeTestRule.setContent {
            ErrorState(
                message = errorMessage,
                onRetry = { retryClicked = true }
            )
        }

        val robot = CountryDetailRobot(composeTestRule)

        robot.assertErrorMessage(errorMessage)
        robot.clickRetry()

        assert(retryClicked)
    }
}