package com.example.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.FactoryInfo
import com.example.domain.model.FilterData
import com.example.domain.model.GyeonggiInfo

@Entity(tableName = "update_db")
data class UpdateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
//    @Embedded val factory: FactoryInfo,
    val time: String,
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
