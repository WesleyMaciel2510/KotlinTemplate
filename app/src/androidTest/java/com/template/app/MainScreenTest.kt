package com.template.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.template.app.ui.screens.MainScreen
import com.template.app.ui.theme.TemplateAppTheme
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreenDisplaysTopBarAndHomeContent() {
        composeTestRule.setContent {
            TemplateAppTheme {
                MainScreen()
            }
        }

        composeTestRule.onNodeWithText("Homescreen Template").assertIsDisplayed()
        composeTestRule.onNodeWithText("Home Screen").assertIsDisplayed()
    }

    @Test
    fun bottomNavigationSwitchesTabs() {
        composeTestRule.setContent {
            TemplateAppTheme {
                MainScreen()
            }
        }

        composeTestRule.onNodeWithText("Home Screen").assertIsDisplayed()

        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.onNodeWithText("Search Screen").assertIsDisplayed()

        composeTestRule.onNodeWithText("Favorites").performClick()
        composeTestRule.onNodeWithText("Favorites Screen").assertIsDisplayed()

        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Profile Screen").assertIsDisplayed()
    }
}