package com.unilibre.finanzasapp.presentation.di

import android.content.Context
import androidx.room.Room
import com.unilibre.finanzasapp.data.local.AppDatabase
import com.unilibre.finanzasapp.data.local.TransaccionDao
import com.unilibre.finanzasapp.data.repository.TransaccionRepositoryImpl
import com.unilibre.finanzasapp.domain.repository.TransaccionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "finanzas_db").build()

    @Provides
    fun provideDao(db: AppDatabase): TransaccionDao = db.transaccionDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: TransaccionRepositoryImpl): TransaccionRepository
}