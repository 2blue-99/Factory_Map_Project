package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FilterEntity
import com.example.data.local.entity.UpdateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateDao {

    @Query("SELECT * FROM update_db")
    fun getAllData(): Flow<List<UpdateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: UpdateEntity)
}