package com.shishusneh.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baby_profiles")
data class BabyProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val dob: String,
    val gender: String,
    val birthWeight: Float,
    val birthHeight: Float,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "growth_records")
data class GrowthRecordEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val date: String,
    val weight: Float?,
    val height: Float?,
    val headCircumference: Float?,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "vaccine_statuses")
data class VaccineStatusEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val vaccineId: String,
    val completed: Boolean,
    val completedDate: String?
)

@Entity(tableName = "milestone_statuses")
data class MilestoneStatusEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val milestoneId: String,
    val completed: Boolean,
    val completedDate: String?
)

@Entity(tableName = "feeding_records")
data class FeedingRecordEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val type: String,
    val startTime: Long,
    val endTime: Long?,
    val durationSeconds: Int,
    val side: String?,
    val amountMl: Float?,
    val foodItem: String?,
    val notes: String = ""
)

@Entity(tableName = "sleep_records")
data class SleepRecordEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val startTime: Long,
    val endTime: Long?,
    val durationSeconds: Int,
    val notes: String = ""
)

@Entity(tableName = "diaper_records")
data class DiaperRecordEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val time: Long,
    val type: String,
    val notes: String = ""
)

@Entity(tableName = "doctor_visits")
data class DoctorVisitEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val date: String,
    val doctorName: String,
    val clinic: String,
    val reason: String,
    val weight: Float?,
    val height: Float?,
    val temperature: Float?,
    val notes: String = ""
)
