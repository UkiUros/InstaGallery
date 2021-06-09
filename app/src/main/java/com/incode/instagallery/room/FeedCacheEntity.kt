package com.incode.instagallery.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeds")
data class FeedCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "title")
    val title: String? = "",

    @ColumnInfo(name = "comment")
    val comment: String? = "",

    @ColumnInfo(name = "picture_uri")
    val pictureUri: String = "",

    @ColumnInfo(name = "published_at")
    val publishedAt: Long,

    @ColumnInfo(name = "from_network")
    val fromNetwork: Int = 0
)
