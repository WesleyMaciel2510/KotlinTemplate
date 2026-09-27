package com.template.app.data

import com.template.app.data.local.ItemDao
import com.template.app.data.local.ItemEntity
import com.template.app.data.remote.NetworkResult
import com.template.app.data.remote.RemoteDataSource
import com.template.app.data.repository.ItemRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemRepositoryTest {

    private class FakeItemDao : ItemDao {
        val items = mutableListOf<ItemEntity>()

        override fun getAllItemsFlow() = flowOf(items.toList())
        override fun getItemByIdFlow(id: String) = flowOf(items.find { it.id == id })
        override suspend fun getItemById(id: String) = items.find { it.id == id }
        override suspend fun insertItem(item: ItemEntity): Long {
            items.add(item)
            return 1L
        }
        override suspend fun insertItems(items: List<ItemEntity>): List<Long> {
            this.items.addAll(items)
            return items.map { 1L }
        }
        override suspend fun updateItem(item: ItemEntity): Int = 1
        override suspend fun deleteItem(item: ItemEntity): Int {
            items.remove(item)
            return 1
        }
        override suspend fun deleteItemById(id: String): Int {
            items.removeAll { it.id == id }
            return 1
        }
        override suspend fun clearAll(): Int {
            items.clear()
            return 1
        }
    }

    @Test
    fun repository_savesAndRetrievesItems() = runTest {
        val fakeDao = FakeItemDao()
        val repository = ItemRepositoryImpl(
            itemDao = fakeDao,
            remoteDataSource = RemoteDataSource(object : com.template.app.data.remote.ApiService {
                override suspend fun getItems() = emptyList<com.template.app.data.remote.ItemDto>()
                override suspend fun getItemById(id: String) = com.template.app.data.remote.ItemDto(id, "Title")
                override suspend fun createItem(item: com.template.app.data.remote.ItemDto) = item
            }),
            ioDispatcher = Dispatchers.Unconfined
        )

        val testItem = ItemEntity("1", "Test Title", "Test Desc", "")
        repository.saveItem(testItem)

        val retrievedItems = repository.getItemsStream().first()
        assertEquals(1, retrievedItems.size)
        assertEquals("Test Title", retrievedItems.first().title)
    }
}
