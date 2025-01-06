package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FactoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactoryDao {
    @Query("SELECT * FROM factory")
    fun getAllData(): Flow<List<FactoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: FactoryEntity)

    @Query("DELETE FROM factory WHERE id = :id")
    fun deleteData(id: Int)

    @Query("DELETE FROM factory")
    fun deleteAllData()
}