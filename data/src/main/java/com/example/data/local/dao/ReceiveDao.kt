package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FilterEntity
import com.example.data.local.entity.ReceiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiveDao {

    @Query("SELECT * FROM receive_db")
    fun getAllData(): Flow<List<ReceiveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: ReceiveEntity)
}