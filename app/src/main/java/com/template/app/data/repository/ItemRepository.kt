package com.template.app.data.repository

import com.template.app.data.local.ItemDao
import com.template.app.data.local.ItemEntity
import com.template.app.data.remote.NetworkResult
import com.template.app.data.remote.RemoteDataSource
import com.template.app.data.remote.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ItemRepository {
    fun getItemsStream(): Flow<List<ItemEntity>>
    fun getItemStream(id: String): Flow<ItemEntity?>
    suspend fun refreshItems(): Result<Unit>
    suspend fun saveItem(item: ItemEntity)
    suspend fun deleteItem(id: String)
}

@Singleton
class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao,
    private val remoteDataSource: RemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ItemRepository {

    override fun getItemsStream(): Flow<List<ItemEntity>> {
        return itemDao.getAllItemsFlow().flowOn(ioDispatcher)
    }

    override fun getItemStream(id: String): Flow<ItemEntity?> {
        return itemDao.getItemByIdFlow(id).flowOn(ioDispatcher)
    }

    override suspend fun refreshItems(): Result<Unit> = withContext(ioDispatcher) {
        when (val result = remoteDataSource.fetchItems()) {
            is NetworkResult.Success -> {
                val entities = result.data.map { it.toEntity() }
                itemDao.insertItems(entities)
                Result.success(Unit)
            }
            is NetworkResult.Error -> {
                Result.failure(result.exception)
            }
        }
    }

    override suspend fun saveItem(item: ItemEntity) {
        withContext(ioDispatcher) {
            itemDao.insertItem(item)
        }
    }

    override suspend fun deleteItem(id: String) {
        withContext(ioDispatcher) {
            itemDao.deleteItemById(id)
        }
    }
}
