package com.template.app.home.data

import com.template.app.home.domain.HomeOverview

interface HomeRepository {
    suspend fun getHomeOverview(): HomeOverview
}
