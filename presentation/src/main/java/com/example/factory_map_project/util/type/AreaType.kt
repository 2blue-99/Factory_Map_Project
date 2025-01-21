package com.example.factory_map_project.util.type

enum class AreaType(
    val title: String
) {
    SEOUL(title = "서울"),
    INCHEON(title = "인천"),
    GYEONGGI(title = "경기도"),
    GANGWON(title = "강원도"),
    SEJONG(title = "세종"),
    NORTH_CHUNGCHEONG(title = "충청북도"),
    SOUTH_CHUNGCHEONG(title = "충청남도"),
    DAEJEON(title = "대전"),
    DAEGU(title = "대구"),
    GWANGJU(title = "광주"),
    ULSAN(title = "울산"),
    BUSAN(title = "부산"),
    NORTH_GYEONGSANG(title = "경상북도"),
    SOUTH_GYEONGSANG(title = "경상남도"),
    NORTH_JEOLLA(title = "전라북도"),
    SOUTH_JEOLLA(title = "전라남도"),
    ;

    companion object {
        fun toPosition(type: AreaType?): Int{
            return entries.indexOf(type)
        }

        fun toType(position: Int): AreaType {
            return entries[position]
        }

        fun toTitleList(): List<String>{
            return entries.map { it.title }
        }
    }
}