package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.TestDao
import com.example.data.local.entity.FactoryEntity

@Database(entities = [FactoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun testDao(): TestDao
}