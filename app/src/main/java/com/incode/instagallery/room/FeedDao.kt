package com.incode.instagallery.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(feedCacheEntity: FeedCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feeds: List<FeedCacheEntity>)

    @Query("SELECT * FROM feeds ORDER BY published_at DESC")
    suspend fun getFeeds(): List<FeedCacheEntity>

    @Query("SELECT * FROM feeds WHERE id = :feedId")
    suspend fun getFeed(feedId: String): FeedCacheEntity

    @Query("DELETE FROM feeds WHERE from_network = 1")
    fun deleteAllNetworkFeeds()
}
