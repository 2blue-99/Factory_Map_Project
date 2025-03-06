package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.local.dao.ReceiveDao
import com.example.data.local.dao.SendDao
import com.example.data.local.entity.FactoryEntity
import com.example.data.local.entity.FilterEntity
import com.example.data.local.entity.ReceiveEntity
import com.example.data.local.entity.SendEntity

@Database(
    entities = [FactoryEntity::class, FilterEntity::class, ReceiveEntity::class, SendEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun factoryDao(): FactoryDao
    abstract fun filterDao(): FilterDao
    abstract fun receiveDao(): ReceiveDao
    abstract fun sendDao(): SendDao
}