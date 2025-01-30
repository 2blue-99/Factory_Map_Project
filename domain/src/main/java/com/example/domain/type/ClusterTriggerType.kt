package com.example.domain.type

enum class ClusterTriggerType(
    val title: String,
    val size: Int // 클러스터 트리거 사이즈
) {
    LOW(title = "약함", 35),
    LITTLE_LOW(title = "다소 약함",30),
    MID(title = "보통",25),
    LITTLE_LARGE(title = "다소 강함",20),
    LARGE(title = "강함",15),
    ;

    companion object {
        fun toPosition(type: ClusterTriggerType?): Int{
            return entries.indexOf(type)
        }

        fun toPosition(title: String?): Int{
            return entries.indexOfFirst { it.title == title }
        }

        fun toType(position: Int): ClusterTriggerType {
            return entries[position]
        }

        fun toList(): List<String>{
            return entries.map { it.title }
        }
    }
}