package com.example.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.FactoryInfo
import com.example.domain.model.FilterData
import com.example.domain.model.GyeonggiInfo

@Entity(tableName = "receive_db")
data class ReceiveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val remoteId: Int,
//    @Embedded val factory: FactoryInfo,
    val time: String,
    val userId: String,
    val isUpdate: Boolean,
    val isDelete: Boolean
){
//    fun toDomain(): FilterData {
//        return FilterData(
//            id = id,
//            target = target,
//            keyword = keyword
//        )
//    }
}
