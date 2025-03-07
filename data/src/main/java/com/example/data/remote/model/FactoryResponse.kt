package com.example.data.remote.model

import com.example.data.local.entity.ReceiveEntity
import com.example.domain.model.FactoryInfo

data class FactoryResponse(
    val userCode: String = "",
    val factory: FactoryInfoResponse,
)

fun Pair<String, FactoryResponse>.toEntity(): ReceiveEntity =
    ReceiveEntity(
        id = 0,
        remoteId = this.first,
        factory = this.second.factory.toDomain(),
        userCode = this.second.userCode,
        isUpdate = false,
        isDelete = false
    )