package com.factory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.factory.data.local.entity.FactoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactoryDao {

    @Query("""
        SELECT COUNT(*) FROM factory 
        WHERE companyName LIKE '%' || :targetName || '%'
        """)
    fun getTargetFactoryDao(targetName: String): Int

    @Query(
        """
        SELECT * FROM factory
        WHERE loadAddress LIKE :area || '%'
        AND isDeleted = 0
        AND latitude BETWEEN (:lat - :range) AND (:lat + :range)
        AND longitude BETWEEN (:lon - :range) AND (:lon + :range)
        AND companyName LIKE '%' || :searchName || '%'
        """
    )
    fun getTargetData(area: String, lat: Double, lon: Double, range: Double, searchName: String): Flow<List<FactoryEntity>>

    @Query("SELECT * FROM factory WHERE id in (:id)")
    fun getTargetIdData(id: List<Int>): List<FactoryEntity>

    @Transaction
    fun upsertDataList(data: List<FactoryEntity>){
        data.forEach { upsertData(it) }
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertData(data: FactoryEntity)


    @Query("UPDATE factory SET isDeleted = 1 WHERE id = :id")
    fun deleteData(id: Int)

    @Query("DELETE FROM factory")
    fun deleteAllData()
}