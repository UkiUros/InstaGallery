package com.incode.instagallery.networking

import com.incode.instagallery.domain.data.Feed
import javax.inject.Inject

class NetworkMapper
@Inject
constructor() {

    fun mapFromEntityList(entities: List<FeedNetworkEntity>): List<Feed> {
        return entities.map { mapFromEntity(it) }
    }

    private fun mapFromEntity(entity: FeedNetworkEntity): Feed {
        return Feed(
            id = entity._id,
            title = entity.title,
            comment = entity.comment,
            pictureUrl = entity.picture,
            publishedAt = entity.publishedAt
        )
    }
}
