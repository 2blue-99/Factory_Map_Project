package com.factory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.factory.data.local.dao.FactoryDao
import com.factory.data.local.dao.FilterDao
import com.factory.data.local.dao.ReceiveDao
import com.factory.data.local.dao.SendDao
import com.factory.data.local.entity.FactoryEntity
import com.factory.data.local.entity.FilterEntity
import com.factory.data.local.entity.ReceiveEntity
import com.factory.data.local.entity.SendEntity

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