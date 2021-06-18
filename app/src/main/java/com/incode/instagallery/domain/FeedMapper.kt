package com.incode.instagallery.domain

import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.networking.FeedNetworkEntity
import com.incode.instagallery.room.FeedCacheEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FeedMapper
@Inject
constructor() {

    fun mapFromCacheEntityList(entities: List<FeedCacheEntity>): List<Feed> {
        return entities.map { mapFromCacheEntity(it) }
    }

    fun mapToCacheEntityList(networkEntities: List<FeedNetworkEntity>): List<FeedCacheEntity> {
        return networkEntities.map { mapToCacheEntity(it) }
    }

    fun mapFromCacheEntity(entity: FeedCacheEntity): Feed {
        return Feed(
            id = entity.id,
            title = entity.title,
            comment = entity.comment,
            pictureUri = entity.pictureUri,
            publishedAt = mapMsToString(entity.publishedAt),
            entity.fromNetwork == 1
        )
    }

    private fun mapToCacheEntity(networkEntity: FeedNetworkEntity): FeedCacheEntity {
        return FeedCacheEntity(
            id = networkEntity._id,
            title = networkEntity.title,
            comment = networkEntity.comment,
            pictureUri = networkEntity.picture,
            publishedAt = mapStringToTimeMs(networkEntity.publishedAt),
            1
        )
    }

    private fun mapStringToTimeMs(dateString: String): Long {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US)
        return formatter.parse(dateString)?.time ?: 0L
    }

    private fun mapMsToString(msTime: Long) : String {
        val date = Date(msTime)
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US)
        return formatter.format(date)
    }
}
