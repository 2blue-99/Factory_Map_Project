package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FilterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {

    @Query("SELECT * FROM filter")
    fun getAllData(): Flow<List<FilterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: FilterEntity)

    @Query("DELETE FROM filter WHERE id = :id")
    fun deleteData(id: Int)
}