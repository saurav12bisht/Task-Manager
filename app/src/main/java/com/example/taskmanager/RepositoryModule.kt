package com.example.taskmanager

import com.example.taskmanager.repository.RepoImpl
import com.example.taskmanager.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {


    @Provides
    @Singleton
    fun provideRepo(

    ): Repository {
        return RepoImpl()
    }
}