package com.unilibre.asistenterecetas.di.module

import android.content.Context
import androidx.room.Room
import com.unilibre.asistenterecetas.data.local.RecetaDao
import com.unilibre.asistenterecetas.data.local.RecetasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RecetasDatabase {
        return Room.databaseBuilder(
            context,
            RecetasDatabase::class.java,
            "recetas_db"
        ).build()
    }

    @Provides
    fun provideRecetaDao(db: RecetasDatabase): RecetaDao = db.recetaDao()
}
