package com.template.app.domain.repository

import com.template.app.domain.model.RecebivelItem
import kotlinx.coroutines.flow.Flow

interface RecebiveisRepository {
    fun getRecebiveis(): Flow<List<RecebivelItem>>
    fun getRecebivelById(id: String): Flow<RecebivelItem?>
}
