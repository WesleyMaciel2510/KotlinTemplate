package com.template.app

import android.app.Application
import com.template.app.di.AppContainer

class TemplateApp : Application() {
    val container = AppContainer()
}