package com.devart.countrieschallange2026.presentation.screens.countries_list

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class CountriesListRobot(private val composeTestRule: ComposeContentTestRule) {

    fun assertCountryVisible(name: String) {
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
    }

    fun assertLoadingVisible() {
        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    fun assertEmptyStateVisible() {
        composeTestRule.onNodeWithText("No countries match your search.").assertIsDisplayed()
    }

    fun typeSearch(query: String) {
        composeTestRule.onNodeWithText("Search by name, region or code...")
            .performTextInput(query)
    }

    fun clickClearSearch() {
        composeTestRule.onNodeWithContentDescription("Clear search").performClick()
    }

    fun clickCountry(name: String) {
        composeTestRule.onNodeWithText(name).performClick()
    }

    fun assertFlagVisible(countryName: String) {
        composeTestRule.onNodeWithContentDescription("$countryName flag").assertExists()
    }
}