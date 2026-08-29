package com.template.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.template.app.di.AppContainer
import com.template.app.navigation.AppNavHost
import com.template.app.ui.theme.TemplateAppTheme

class MainActivity : ComponentActivity() {
    private val appContainer: AppContainer by lazy { (application as TemplateApp).container }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemplateAppTheme {
                AppNavHost(container = appContainer)
            }
        }
    }
}