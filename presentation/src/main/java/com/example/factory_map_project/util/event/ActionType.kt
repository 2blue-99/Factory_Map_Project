package com.example.factory_map_project.util.event

enum class ActionType {
    // Bottom Sheet
    NEGATIVE,
    CONFIRM,
    DELETE,

    // Move
    CALL,
    MAP,

    // Setting
    EXCLUSION, // 노출 공장 중, 제외 단어

    // Map
    POSITION_INIT,
    MY_LOCATION,
}