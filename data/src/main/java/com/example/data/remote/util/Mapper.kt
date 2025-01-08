package com.example.data.remote.util

import com.example.data.local.entity.FactoryEntity
import com.example.domain.model.AllAreaInfo

object Mapper {
    fun String?.toDateStringFormat(): String {
        return if(this?.length == 8){
            val year = this.substring(0,4)
            val month = this.substring(4,6)
            val day = this.substring(6,8)
            "${year}. ${month}. ${day}"
        }else{
            "확인 불가"
        }
    }
}