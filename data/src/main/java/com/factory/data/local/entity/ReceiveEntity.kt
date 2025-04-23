package com.factory.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.factory.domain.model.FactoryInfo

@Entity(tableName = "receive_db")
data class ReceiveEntity(
    @PrimaryKey(autoGenerate = true)
    val receiveId: Int,
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
