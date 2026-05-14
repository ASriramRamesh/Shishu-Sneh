package com.shishusneh.app.data.model

data class Vaccine(
    val id: String,
    val name: String,
    val ageMonths: Int,
    val description: String,
    val diseases: List<String>
)

val INDIAN_NIS_VACCINES = listOf(
    Vaccine("bcg", "BCG", 0, "Bacille Calmette-Guérin vaccine", listOf("Tuberculosis")),
    Vaccine("opv0", "OPV (Birth dose)", 0, "Oral Polio Vaccine", listOf("Poliomyelitis")),
    Vaccine("hepb1", "Hepatitis B (Birth)", 0, "Hepatitis B vaccine – first dose", listOf("Hepatitis B")),
    Vaccine("dpt1", "DPT – 1st dose", 1, "Diphtheria, Pertussis & Tetanus", listOf("Diphtheria","Pertussis","Tetanus")),
    Vaccine("opv1", "OPV – 1st dose", 1, "Oral Polio Vaccine – 1st dose", listOf("Poliomyelitis")),
    Vaccine("ipv1", "IPV – 1st dose", 1, "Inactivated Poliovirus Vaccine", listOf("Poliomyelitis")),
    Vaccine("hib1", "Hib – 1st dose", 1, "Haemophilus influenzae type b", listOf("Meningitis","Pneumonia")),
    Vaccine("hepb2", "Hepatitis B – 2nd dose", 1, "Hepatitis B vaccine – second dose", listOf("Hepatitis B")),
    Vaccine("rota1", "Rotavirus – 1st dose", 1, "Rotavirus vaccine", listOf("Rotavirus diarrhoea")),
    Vaccine("pcv1", "PCV – 1st dose", 1, "Pneumococcal Conjugate Vaccine", listOf("Pneumonia","Meningitis")),
    Vaccine("dpt2", "DPT – 2nd dose", 2, "DPT – second dose", listOf("Diphtheria","Pertussis","Tetanus")),
    Vaccine("opv2", "OPV – 2nd dose", 2, "OPV – second dose", listOf("Poliomyelitis")),
    Vaccine("hib2", "Hib – 2nd dose", 2, "Hib – second dose", listOf("Meningitis","Pneumonia")),
    Vaccine("hepb3", "Hepatitis B – 3rd dose", 2, "Hepatitis B – third dose", listOf("Hepatitis B")),
    Vaccine("rota2", "Rotavirus – 2nd dose", 2, "Rotavirus – second dose", listOf("Rotavirus diarrhoea")),
    Vaccine("pcv2", "PCV – 2nd dose", 2, "PCV – second dose", listOf("Pneumonia","Meningitis")),
    Vaccine("dpt3", "DPT – 3rd dose", 4, "DPT – third dose", listOf("Diphtheria","Pertussis","Tetanus")),
    Vaccine("opv3", "OPV – 3rd dose", 4, "OPV – third dose", listOf("Poliomyelitis")),
    Vaccine("ipv2", "IPV – 2nd dose", 4, "IPV – second dose", listOf("Poliomyelitis")),
    Vaccine("hib3", "Hib – 3rd dose", 4, "Hib – third dose", listOf("Meningitis","Pneumonia")),
    Vaccine("rota3", "Rotavirus – 3rd dose", 4, "Rotavirus – third dose", listOf("Rotavirus diarrhoea")),
    Vaccine("pcv3", "PCV – 3rd dose", 4, "PCV – booster dose", listOf("Pneumonia","Meningitis")),
    Vaccine("measles1", "Measles-Rubella 1st dose", 9, "MR vaccine – first dose", listOf("Measles","Rubella")),
    Vaccine("je1", "JE – 1st dose", 9, "Japanese Encephalitis", listOf("Japanese Encephalitis")),
    Vaccine("vitA1", "Vitamin A – 1st dose", 9, "Vitamin A supplementation", listOf("Vitamin A deficiency")),
    Vaccine("dpt_b1", "DPT Booster 1", 16, "DPT booster", listOf("Diphtheria","Pertussis","Tetanus")),
    Vaccine("opv_b", "OPV Booster", 16, "OPV booster", listOf("Poliomyelitis")),
    Vaccine("measles2", "Measles-Rubella 2nd dose", 16, "MR vaccine – second dose", listOf("Measles","Rubella")),
    Vaccine("je2", "JE – 2nd dose", 16, "JE booster", listOf("Japanese Encephalitis")),
    Vaccine("typhoid", "Typhoid Conjugate Vaccine", 9, "Typhoid vaccine", listOf("Typhoid"))
)

fun generateVaccineSchedule(dobMillis: Long): List<Pair<Vaccine, Long>> {
    return INDIAN_NIS_VACCINES.map { vaccine ->
        val dueMs = dobMillis + (vaccine.ageMonths * 30.44 * 24 * 60 * 60 * 1000).toLong()
        vaccine to dueMs
    }.sortedBy { it.second }
}
