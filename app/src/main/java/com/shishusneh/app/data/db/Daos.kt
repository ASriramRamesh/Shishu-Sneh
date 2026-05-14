package com.shishusneh.app.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyProfileDao {
    @Query("SELECT * FROM baby_profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): Flow<List<BabyProfileEntity>>

    @Query("SELECT * FROM baby_profiles WHERE id = :id")
    suspend fun getProfileById(id: String): BabyProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BabyProfileEntity)

    @Update
    suspend fun updateProfile(profile: BabyProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: BabyProfileEntity)

    @Query("DELETE FROM baby_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: String)
}

@Dao
interface GrowthRecordDao {
    @Query("SELECT * FROM growth_records WHERE profileId = :profileId ORDER BY date DESC")
    fun getRecordsForProfile(profileId: String): Flow<List<GrowthRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: GrowthRecordEntity)

    @Delete
    suspend fun deleteRecord(record: GrowthRecordEntity)

    @Query("DELETE FROM growth_records WHERE id = :id")
    suspend fun deleteRecordById(id: String)
}

@Dao
interface VaccineStatusDao {
    @Query("SELECT * FROM vaccine_statuses WHERE profileId = :profileId")
    fun getStatusesForProfile(profileId: String): Flow<List<VaccineStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(status: VaccineStatusEntity)

    @Query("DELETE FROM vaccine_statuses WHERE id = :id")
    suspend fun deleteStatusById(id: String)
}

@Dao
interface MilestoneStatusDao {
    @Query("SELECT * FROM milestone_statuses WHERE profileId = :profileId")
    fun getStatusesForProfile(profileId: String): Flow<List<MilestoneStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(status: MilestoneStatusEntity)

    @Query("DELETE FROM milestone_statuses WHERE id = :id")
    suspend fun deleteStatusById(id: String)
}

@Dao
interface FeedingRecordDao {
    @Query("SELECT * FROM feeding_records WHERE profileId = :profileId ORDER BY startTime DESC")
    fun getRecordsForProfile(profileId: String): Flow<List<FeedingRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: FeedingRecordEntity)

    @Delete
    suspend fun deleteRecord(record: FeedingRecordEntity)

    @Query("DELETE FROM feeding_records WHERE id = :id")
    suspend fun deleteRecordById(id: String)
}

@Dao
interface SleepRecordDao {
    @Query("SELECT * FROM sleep_records WHERE profileId = :profileId ORDER BY startTime DESC")
    fun getRecordsForProfile(profileId: String): Flow<List<SleepRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: SleepRecordEntity)

    @Query("DELETE FROM sleep_records WHERE id = :id")
    suspend fun deleteRecordById(id: String)
}

@Dao
interface DiaperRecordDao {
    @Query("SELECT * FROM diaper_records WHERE profileId = :profileId ORDER BY time DESC")
    fun getRecordsForProfile(profileId: String): Flow<List<DiaperRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: DiaperRecordEntity)

    @Query("DELETE FROM diaper_records WHERE id = :id")
    suspend fun deleteRecordById(id: String)
}

@Dao
interface DoctorVisitDao {
    @Query("SELECT * FROM doctor_visits WHERE profileId = :profileId ORDER BY date DESC")
    fun getVisitsForProfile(profileId: String): Flow<List<DoctorVisitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: DoctorVisitEntity)

    @Delete
    suspend fun deleteVisit(visit: DoctorVisitEntity)

    @Query("DELETE FROM doctor_visits WHERE id = :id")
    suspend fun deleteVisitById(id: String)
}
