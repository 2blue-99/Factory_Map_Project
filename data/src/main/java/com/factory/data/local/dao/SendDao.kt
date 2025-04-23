package com.factory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.factory.data.local.entity.SendEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SendDao {

    @Query("SELECT * FROM send_db")
    fun getAllData(): Flow<List<SendEntity>>

    @Query("SELECT * FROM send_db WHERE isUpdate = 0")
    suspend fun getNotUpdateData(): List<SendEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: SendEntity): Long
}