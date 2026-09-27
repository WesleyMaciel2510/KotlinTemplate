package com.template.app.home.domain

data class HomeOverview(
    val greeting: String,
    val summary: String,
    val metrics: List<HomeMetric>,
    val recentItems: List<HomeItem>
)

data class HomeMetric(
    val label: String,
    val value: String
)

data class HomeItem(
    val title: String,
    val detail: String
)
