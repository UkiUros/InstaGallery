package com.incode.instagallery.repository

import android.net.Uri
import com.incode.instagallery.domain.DataState
import com.incode.instagallery.domain.FeedMapper
import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.networking.FeedService
import com.incode.instagallery.room.FeedCacheEntity
import com.incode.instagallery.room.FeedDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FeedRepository constructor(
    private val feedService: FeedService,
    private val feedDao: FeedDao,
    private val feedMapper: FeedMapper
) {

    @ExperimentalCoroutinesApi
    suspend fun getFeedForId(id: String): Flow<Feed?> = flow {
        val cachedFeeds = feedDao.getFeed(id)
        emit(feedMapper.mapFromCacheEntity(cachedFeeds))
    }

    @ExperimentalCoroutinesApi
    suspend fun getFeeds(): Flow<DataState<List<Feed>>> = flow {
        emit(DataState.Loading)

        val networkFeeds = feedService.getFeeds()
        val mappedCacheFeeds = feedMapper.mapToCacheEntityList(networkFeeds)

        feedDao.deleteAllNetworkFeeds()
        feedDao.insertAll(mappedCacheFeeds)

        val cachedFeeds = feedDao.getFeeds()
        emit(DataState.Success(feedMapper.mapFromCacheEntityList(cachedFeeds)))

    }

    @ExperimentalCoroutinesApi
    suspend fun getLocalFeeds(): Flow<DataState<List<Feed>>> = flow {
        emit(DataState.Loading)

        val cachedFeeds = feedDao.getFeeds()
        emit(DataState.Success(feedMapper.mapFromCacheEntityList(cachedFeeds)))

    }.catch {
        emit(DataState.Error(it))
    }

    suspend fun saveLocalPhoto(uriString: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        val path = Uri.parse(uriString).path ?: throw IllegalArgumentException("Invalid URI path")

        val currentMs = System.currentTimeMillis()
        val cacheEntity = FeedCacheEntity(
            id = currentMs.toString(),
            pictureUri = path,
            publishedAt = currentMs
        )

        val result = feedDao.insert(cacheEntity)

        emit(DataState.Success(result > -1))

    }.catch {
        emit(DataState.Error(it))
    }
}
