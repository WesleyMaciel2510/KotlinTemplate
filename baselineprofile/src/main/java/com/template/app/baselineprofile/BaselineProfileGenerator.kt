package com.template.app.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() {
        rule.collect(
            packageName = "com.template.app",
            includeInStartupProfile = true
        ) {
            // Start the app
            startActivityAndWait()
            
            // Wait for the UI to be drawn
            device.waitForIdle()
            
            // Scroll through Home screen content to capture UI paths
            // The Home screen has a LazyColumn that we can scroll
            device.executeShellCommand("input swipe 500 1500 500 500 300")
            device.waitForIdle()
            
            // Additional scroll interactions
            device.executeShellCommand("input swipe 500 1500 500 500 300")
            device.waitForIdle()
        }
    }
}