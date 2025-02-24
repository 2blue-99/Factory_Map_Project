package com.example.factory_map_project.util

enum class PopupContent(
    val title: String,
    val content: String
) {
    // APP
    NETWORK_ERR(title = "", content = ""),
    UNKNOWN_ERR(title = "", content = ""),
    NOTICE_PERMISSION(title = "알림", content = "앱 이용을 위해 설정에서\n%s을 허용해주세요."),


    // MAP
    MAP_MARKER_DELETE(title = "알림", content = "정말 삭제하시겠습니까"),
    MAP_ADD_ITEM(title = "알림", content = "장소를 추가하시겠습니까?"),
    MAP_NO_MATCH_AREA(title = "알림", content = "상단의 선택한 지역[%s]과 추가할 업체의 지역[%s]이 다릅니다.\n\n[%s]로 지역을 변경하고 진행하시겠습니까?"),
}