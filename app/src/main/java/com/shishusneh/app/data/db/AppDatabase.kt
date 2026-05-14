package com.shishusneh.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BabyProfileEntity::class,
        GrowthRecordEntity::class,
        VaccineStatusEntity::class,
        MilestoneStatusEntity::class,
        FeedingRecordEntity::class,
        SleepRecordEntity::class,
        DiaperRecordEntity::class,
        DoctorVisitEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun growthRecordDao(): GrowthRecordDao
    abstract fun vaccineStatusDao(): VaccineStatusDao
    abstract fun milestoneStatusDao(): MilestoneStatusDao
    abstract fun feedingRecordDao(): FeedingRecordDao
    abstract fun sleepRecordDao(): SleepRecordDao
    abstract fun diaperRecordDao(): DiaperRecordDao
    abstract fun doctorVisitDao(): DoctorVisitDao
}
