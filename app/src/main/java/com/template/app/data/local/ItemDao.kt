package com.template.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY timestamp DESC")
    fun getAllItemsFlow(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemByIdFlow(id: String): Flow<ItemEntity?>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: String): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>): List<Long>

    @Update
    suspend fun updateItem(item: ItemEntity): Int

    @Delete
    suspend fun deleteItem(item: ItemEntity): Int

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteItemById(id: String): Int

    @Query("DELETE FROM items")
    suspend fun clearAll(): Int
}
