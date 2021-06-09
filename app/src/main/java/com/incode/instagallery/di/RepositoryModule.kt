package com.incode.instagallery.di

import com.incode.instagallery.domain.FeedMapper
import com.incode.instagallery.networking.FeedService
import com.incode.instagallery.repository.FeedRepository
import com.incode.instagallery.room.FeedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideArticleRepository(
        service: FeedService,
        feedMapper: FeedMapper,
        feedDao: FeedDao
    ): FeedRepository {
        return FeedRepository(service, feedDao, feedMapper)
    }
}
