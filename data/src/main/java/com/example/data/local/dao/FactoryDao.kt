package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.FactoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactoryDao {

    @Query("SELECT * FROM factory")
    fun getAllData(): Flow<List<FactoryEntity>>

    @Query(
        """
        SELECT * FROM factory
        WHERE loadAddress LIKE :area || '%'
        AND latitude BETWEEN (:lat - :range) AND (:lat + :range)
        AND longitude BETWEEN (:lon - :range) AND (:lon + :range)
        """
    )
    fun getTargetData(area: String, lat: Double, lon: Double, range: Double): Flow<List<FactoryEntity>>

    @Transaction
    fun upsertDataList(data: List<FactoryEntity>){
        data.forEach { upsertData(it) }
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: FactoryEntity)


    @Query("DELETE FROM factory WHERE id = :id")
    fun deleteData(id: Int)

    @Query("DELETE FROM factory")
    fun deleteAllData()
}