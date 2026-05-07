package com.unilibre.asistenterecetas.di.module

import com.unilibre.asistenterecetas.data.repository.AiRepositoryImpl
import com.unilibre.asistenterecetas.data.repository.RecipeRepositoryImpl
import com.unilibre.asistenterecetas.data.repository.VisionRepositoryImpl
import com.unilibre.asistenterecetas.domain.repository.AiRepository
import com.unilibre.asistenterecetas.domain.repository.RecipeRepository
import com.unilibre.asistenterecetas.domain.repository.VisionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVisionRepository(
        visionRepositoryImpl: VisionRepositoryImpl
    ): VisionRepository

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        aiRepositoryImpl: AiRepositoryImpl
    ): AiRepository

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
}
