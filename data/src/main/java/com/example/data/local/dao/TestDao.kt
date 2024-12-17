package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.TestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {
    @Query("SELECT * FROM test")
    fun getAllData(): Flow<List<TestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: TestEntity)

    @Query("DELETE FROM test WHERE id = :id")
    fun deleteData(id: Int)
}