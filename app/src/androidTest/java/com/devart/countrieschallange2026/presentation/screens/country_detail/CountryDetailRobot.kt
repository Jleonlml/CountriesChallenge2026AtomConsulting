package com.devart.countrieschallange2026.presentation.screens.country_detail

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule

class CountryDetailRobot(private val composeTestRule: ComposeContentTestRule) {

    fun assertCountryName(name: String) {
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
    }

    fun assertDetail(label: String, value: String) {
        composeTestRule.onNodeWithText(label).assertExists()
        composeTestRule.onNodeWithText(value).assertIsDisplayed()
    }

    fun assertLoadingVisible() {
        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    fun assertErrorMessage(message: String) {
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    fun clickRetry() {
        composeTestRule.onNodeWithText("Retry").performClick()
    }

    fun clickBack() {
        composeTestRule.onNodeWithContentDescription("Back").performClick()
    }
}