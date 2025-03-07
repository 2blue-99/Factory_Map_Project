package com.example.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.FactoryInfo

@Entity(tableName = "receive_db")
data class ReceiveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val remoteId: String,
    @Embedded val factory: FactoryInfo,
    val userCode: String,
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
