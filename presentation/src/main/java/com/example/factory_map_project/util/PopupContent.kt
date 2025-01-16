package com.example.factory_map_project.util

enum class PopupContent(
    val title: String,
    val content: String
) {
    // APP
    NETWORK_ERR(title = "", content = ""),
    UNKNOWN_ERR(title = "", content = ""),

    // MAP
    MAP_MARKER_DELETE(title = "알림", content = "정말 삭제하시겠습니까")
}