package com.incode.instagallery.repository

import com.incode.instagallery.domain.DataState
import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.networking.FeedService
import com.incode.instagallery.networking.NetworkMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FeedRepository constructor(
    private val feedService: FeedService,
    private val networkMapper: NetworkMapper
) {

    @ExperimentalCoroutinesApi
    suspend fun getFeeds(): Flow<DataState<List<Feed>>> = flow {
        emit(DataState.Loading)

        val networkFeeds = feedService.getFeeds()
        emit(DataState.Success(networkMapper.mapFromEntityList(networkFeeds)))

    }.catch {
        emit(DataState.Error(it))
    }
}
