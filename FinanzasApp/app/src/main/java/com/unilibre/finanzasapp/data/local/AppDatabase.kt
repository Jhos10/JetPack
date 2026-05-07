package com.unilibre.finanzasapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransaccionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao
}