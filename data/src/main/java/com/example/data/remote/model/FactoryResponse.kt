package com.example.data.remote.model

import com.example.domain.model.FactoryInfo

data class FactoryResponse(
    val id: Int = 0,
    val userId: String = "",
    val factory: FactoryInfo = FactoryInfo.empty(),
    val time: String = ""
)