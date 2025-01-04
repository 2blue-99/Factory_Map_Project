package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FactoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactoryDao {
    @Query("SELECT * FROM test")
    fun getAllData(): Flow<List<FactoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: FactoryEntity)

    @Query("DELETE FROM test WHERE id = :id")
    fun deleteData(id: Int)

    @Query("DELETE FROM test")
    fun deleteAllData()
}