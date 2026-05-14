package com.shishusneh.app.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.db.*
import com.shishusneh.app.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private val KEY_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
private val KEY_ACTIVE_PROFILE = stringPreferencesKey("active_profile_id")

fun generateId(): String = "${System.currentTimeMillis()}${(Math.random() * 1_000_000).toLong()}"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AppViewModel @Inject constructor(
    private val repo: AppRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    // ─── Preferences ─────────────────────────────────────────────────────────
    val hasSeenOnboarding: StateFlow<Boolean> = dataStore.data
        .map { it[KEY_ONBOARDING] ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val activeProfileId: StateFlow<String?> = dataStore.data
        .map { it[KEY_ACTIVE_PROFILE] }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // ─── Profiles ────────────────────────────────────────────────────────────
    val profiles: StateFlow<List<BabyProfileEntity>> = repo.getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val activeProfile: StateFlow<BabyProfileEntity?> = combine(profiles, activeProfileId) { list, id ->
        list.firstOrNull { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // ─── Records scoped to active profile ────────────────────────────────────
    val growthRecords: StateFlow<List<GrowthRecordEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getGrowthRecords(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val vaccineStatuses: StateFlow<List<VaccineStatusEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getVaccineStatuses(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val milestoneStatuses: StateFlow<List<MilestoneStatusEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getMilestoneStatuses(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val feedingRecords: StateFlow<List<FeedingRecordEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getFeedingRecords(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val sleepRecords: StateFlow<List<SleepRecordEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getSleepRecords(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val diaperRecords: StateFlow<List<DiaperRecordEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getDiaperRecords(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val doctorVisits: StateFlow<List<DoctorVisitEntity>> = activeProfileId.flatMapLatest { id ->
        if (id != null) repo.getDoctorVisits(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ─── Preferences ops ─────────────────────────────────────────────────────
    fun markOnboardingComplete() = viewModelScope.launch {
        dataStore.edit { it[KEY_ONBOARDING] = true }
    }

    fun setActiveProfile(id: String) = viewModelScope.launch {
        dataStore.edit { it[KEY_ACTIVE_PROFILE] = id }
    }

    // ─── Profile ops ─────────────────────────────────────────────────────────
    fun addProfile(name: String, dob: String, gender: String, weightKg: Float, heightCm: Float) =
        viewModelScope.launch {
            val id = generateId()
            repo.insertProfile(
                BabyProfileEntity(id, name.trim(), dob, gender, weightKg, heightCm)
            )
            dataStore.edit { it[KEY_ACTIVE_PROFILE] = id }
        }

    fun updateProfile(profile: BabyProfileEntity) = viewModelScope.launch {
        repo.updateProfile(profile)
    }

    fun deleteProfile(id: String) = viewModelScope.launch {
        repo.deleteProfileById(id)
        if (activeProfileId.value == id) {
            val remaining = profiles.value.firstOrNull { it.id != id }
            dataStore.edit { prefs ->
                if (remaining != null) prefs[KEY_ACTIVE_PROFILE] = remaining.id
                else prefs.remove(KEY_ACTIVE_PROFILE)
            }
        }
    }

    // ─── Growth ops ──────────────────────────────────────────────────────────
    fun addGrowthRecord(weight: Float?, height: Float?, head: Float?, notes: String) =
        viewModelScope.launch {
            val profileId = activeProfileId.value ?: return@launch
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repo.insertGrowthRecord(
                GrowthRecordEntity(generateId(), profileId, today, weight, height, head, notes)
            )
        }

    fun deleteGrowthRecord(id: String) = viewModelScope.launch { repo.deleteGrowthRecord(id) }

    // ─── Vaccine ops ─────────────────────────────────────────────────────────
    fun toggleVaccine(vaccineId: String, completed: Boolean) = viewModelScope.launch {
        val profileId = activeProfileId.value ?: return@launch
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val existing = vaccineStatuses.value.firstOrNull { it.vaccineId == vaccineId }
        val id = existing?.id ?: generateId()
        repo.insertVaccineStatus(
            VaccineStatusEntity(id, profileId, vaccineId, completed, if (completed) today else null)
        )
    }

    // ─── Milestone ops ────────────────────────────────────────────────────────
    fun toggleMilestone(milestoneId: String, completed: Boolean) = viewModelScope.launch {
        val profileId = activeProfileId.value ?: return@launch
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val existing = milestoneStatuses.value.firstOrNull { it.milestoneId == milestoneId }
        val id = existing?.id ?: generateId()
        repo.insertMilestoneStatus(
            MilestoneStatusEntity(id, profileId, milestoneId, completed, if (completed) today else null)
        )
    }

    // ─── Feeding ops ─────────────────────────────────────────────────────────
    fun addFeedingRecord(
        type: String, startTime: Long, endTime: Long?, durationSecs: Int,
        side: String?, amountMl: Float?, foodItem: String?, notes: String
    ) = viewModelScope.launch {
        val profileId = activeProfileId.value ?: return@launch
        repo.insertFeedingRecord(
            FeedingRecordEntity(generateId(), profileId, type, startTime, endTime, durationSecs, side, amountMl, foodItem, notes)
        )
    }

    fun deleteFeedingRecord(id: String) = viewModelScope.launch { repo.deleteFeedingRecord(id) }

    // ─── Sleep ops ───────────────────────────────────────────────────────────
    fun addSleepRecord(startTime: Long, endTime: Long?, durationSecs: Int, notes: String) =
        viewModelScope.launch {
            val profileId = activeProfileId.value ?: return@launch
            repo.insertSleepRecord(SleepRecordEntity(generateId(), profileId, startTime, endTime, durationSecs, notes))
        }

    fun deleteSleepRecord(id: String) = viewModelScope.launch { repo.deleteSleepRecord(id) }

    // ─── Diaper ops ──────────────────────────────────────────────────────────
    fun addDiaperRecord(type: String, notes: String = "") = viewModelScope.launch {
        val profileId = activeProfileId.value ?: return@launch
        repo.insertDiaperRecord(DiaperRecordEntity(generateId(), profileId, System.currentTimeMillis(), type, notes))
    }

    fun deleteDiaperRecord(id: String) = viewModelScope.launch { repo.deleteDiaperRecord(id) }

    // ─── Doctor ops ──────────────────────────────────────────────────────────
    fun addDoctorVisit(
        date: String, doctor: String, clinic: String, reason: String,
        weight: Float?, height: Float?, temp: Float?, notes: String
    ) = viewModelScope.launch {
        val profileId = activeProfileId.value ?: return@launch
        repo.insertDoctorVisit(DoctorVisitEntity(generateId(), profileId, date, doctor, clinic, reason, weight, height, temp, notes))
    }

    fun deleteDoctorVisit(id: String) = viewModelScope.launch { repo.deleteDoctorVisit(id) }

    // ─── Helpers ─────────────────────────────────────────────────────────────
    fun getBabyAgeMonths(): Int {
        val dob = activeProfile.value?.dob ?: return 0
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dobDate = sdf.parse(dob) ?: return 0
            val diff = Date().time - dobDate.time
            (diff / (1000L * 60 * 60 * 24 * 30.44)).toInt()
        } catch (e: Exception) { 0 }
    }
}
