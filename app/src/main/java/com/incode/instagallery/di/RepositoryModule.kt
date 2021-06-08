package com.incode.instagallery.di

import com.incode.instagallery.networking.FeedService
import com.incode.instagallery.networking.NetworkMapper
import com.incode.instagallery.repository.FeedRepository
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
        networkMapper: NetworkMapper
    ): FeedRepository {
        return FeedRepository(service, networkMapper)
    }
}
