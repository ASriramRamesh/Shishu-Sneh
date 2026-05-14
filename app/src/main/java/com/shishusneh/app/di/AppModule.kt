package com.shishusneh.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.shishusneh.app.data.db.*
import com.shishusneh.app.data.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shishu_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "shishu_sneh_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideBabyProfileDao(db: AppDatabase) = db.babyProfileDao()
    @Provides fun provideGrowthRecordDao(db: AppDatabase) = db.growthRecordDao()
    @Provides fun provideVaccineStatusDao(db: AppDatabase) = db.vaccineStatusDao()
    @Provides fun provideMilestoneStatusDao(db: AppDatabase) = db.milestoneStatusDao()
    @Provides fun provideFeedingRecordDao(db: AppDatabase) = db.feedingRecordDao()
    @Provides fun provideSleepRecordDao(db: AppDatabase) = db.sleepRecordDao()
    @Provides fun provideDiaperRecordDao(db: AppDatabase) = db.diaperRecordDao()
    @Provides fun provideDoctorVisitDao(db: AppDatabase) = db.doctorVisitDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}
