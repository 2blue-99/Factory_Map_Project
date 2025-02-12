package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.FactoryInfo
import com.example.domain.model.FilterData
import com.example.domain.model.GyeonggiInfo

@Entity(tableName = "filter")
data class FilterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val target: String,
    val keyword: String
){
    fun toDomain(): FilterData {
        return FilterData(
            id = id,
            target = target,
            keyword = keyword
        )
    }
}
