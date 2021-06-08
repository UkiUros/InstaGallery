package com.incode.instagallery.networking

import retrofit2.http.GET

interface FeedService {

    @GET("json/get/cftPFNNHsi")
    suspend fun getFeeds(): List<FeedNetworkEntity>

}
