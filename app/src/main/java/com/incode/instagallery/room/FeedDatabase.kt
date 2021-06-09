package com.incode.instagallery.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FeedCacheEntity::class], version = 1)
abstract class FeedDatabase : RoomDatabase() {

    abstract fun feedDao(): FeedDao

    companion object {
        val DATABASE_NAME = "feeds_db"
    }
}
