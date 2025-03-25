package com.example.data.remote.model

import com.example.data.local.entity.ReceiveEntity

data class FactoryResponse(
    val userCode: String = "",
    val factory: FactoryInfoResponse? = null,
)

fun Pair<String, FactoryResponse>.toEntity(): ReceiveEntity =
    ReceiveEntity(
        receiveId = 0,
        remoteId = this.first,
        factory = this.second.factory!!.toDomain(),
        userCode = this.second.userCode,
        isUpdate = false,
        isDelete = false
    )