package com.factory.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "send_db")
data class SendEntity(
    @PrimaryKey(autoGenerate = true)
    val sendId: Int,
    @Embedded val factory: FactoryEntity,
    val isUpdate: Boolean
){
//    fun toDomain(): FilterData {
//        return FilterData(
//            id = id,
//            target = target,
//            keyword = keyword
//        )
//    }
}
