package com.incode.instagallery.di

import android.content.Context
import androidx.room.Room
import com.incode.instagallery.room.FeedDao
import com.incode.instagallery.room.FeedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideArticleDb(@ApplicationContext context: Context): FeedDatabase {
        return Room
            .databaseBuilder(
                context,
                FeedDatabase::class.java,
                FeedDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFeedDAO(database: FeedDatabase): FeedDao {
        return database.feedDao()
    }

}
