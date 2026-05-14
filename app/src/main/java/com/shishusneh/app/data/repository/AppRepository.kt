package com.shishusneh.app.data.repository

import com.shishusneh.app.data.db.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val babyProfileDao: BabyProfileDao,
    private val growthRecordDao: GrowthRecordDao,
    private val vaccineStatusDao: VaccineStatusDao,
    private val milestoneStatusDao: MilestoneStatusDao,
    private val feedingRecordDao: FeedingRecordDao,
    private val sleepRecordDao: SleepRecordDao,
    private val diaperRecordDao: DiaperRecordDao,
    private val doctorVisitDao: DoctorVisitDao
) {
    // ─── Profiles ────────────────────────────────────────────────────────────
    fun getAllProfiles(): Flow<List<BabyProfileEntity>> = babyProfileDao.getAllProfiles()
    suspend fun insertProfile(profile: BabyProfileEntity) = babyProfileDao.insertProfile(profile)
    suspend fun updateProfile(profile: BabyProfileEntity) = babyProfileDao.updateProfile(profile)
    suspend fun deleteProfileById(id: String) = babyProfileDao.deleteProfileById(id)

    // ─── Growth ───────────────────────────────────────────────────────────────
    fun getGrowthRecords(profileId: String): Flow<List<GrowthRecordEntity>> =
        growthRecordDao.getRecordsForProfile(profileId)
    suspend fun insertGrowthRecord(record: GrowthRecordEntity) = growthRecordDao.insertRecord(record)
    suspend fun deleteGrowthRecord(id: String) = growthRecordDao.deleteRecordById(id)

    // ─── Vaccines ────────────────────────────────────────────────────────────
    fun getVaccineStatuses(profileId: String): Flow<List<VaccineStatusEntity>> =
        vaccineStatusDao.getStatusesForProfile(profileId)
    suspend fun insertVaccineStatus(status: VaccineStatusEntity) = vaccineStatusDao.insertStatus(status)

    // ─── Milestones ──────────────────────────────────────────────────────────
    fun getMilestoneStatuses(profileId: String): Flow<List<MilestoneStatusEntity>> =
        milestoneStatusDao.getStatusesForProfile(profileId)
    suspend fun insertMilestoneStatus(status: MilestoneStatusEntity) = milestoneStatusDao.insertStatus(status)

    // ─── Feeding ─────────────────────────────────────────────────────────────
    fun getFeedingRecords(profileId: String): Flow<List<FeedingRecordEntity>> =
        feedingRecordDao.getRecordsForProfile(profileId)
    suspend fun insertFeedingRecord(record: FeedingRecordEntity) = feedingRecordDao.insertRecord(record)
    suspend fun deleteFeedingRecord(id: String) = feedingRecordDao.deleteRecordById(id)

    // ─── Sleep ───────────────────────────────────────────────────────────────
    fun getSleepRecords(profileId: String): Flow<List<SleepRecordEntity>> =
        sleepRecordDao.getRecordsForProfile(profileId)
    suspend fun insertSleepRecord(record: SleepRecordEntity) = sleepRecordDao.insertRecord(record)
    suspend fun deleteSleepRecord(id: String) = sleepRecordDao.deleteRecordById(id)

    // ─── Diaper ──────────────────────────────────────────────────────────────
    fun getDiaperRecords(profileId: String): Flow<List<DiaperRecordEntity>> =
        diaperRecordDao.getRecordsForProfile(profileId)
    suspend fun insertDiaperRecord(record: DiaperRecordEntity) = diaperRecordDao.insertRecord(record)
    suspend fun deleteDiaperRecord(id: String) = diaperRecordDao.deleteRecordById(id)

    // ─── Doctor Visits ───────────────────────────────────────────────────────
    fun getDoctorVisits(profileId: String): Flow<List<DoctorVisitEntity>> =
        doctorVisitDao.getVisitsForProfile(profileId)
    suspend fun insertDoctorVisit(visit: DoctorVisitEntity) = doctorVisitDao.insertVisit(visit)
    suspend fun deleteDoctorVisit(id: String) = doctorVisitDao.deleteVisitById(id)
}
