package com.factory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.factory.domain.model.FilterData

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
