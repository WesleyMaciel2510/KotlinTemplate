package com.template.app.presentation.home

import com.template.app.domain.model.ExampleItem

sealed interface HomeUiEvent {
    object LoadItems : HomeUiEvent
    data class ItemClicked(val item: ExampleItem) : HomeUiEvent
}