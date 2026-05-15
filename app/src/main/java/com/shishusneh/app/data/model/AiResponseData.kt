package com.shishusneh.app.data.model

data class AiResponse(
    val keywords: List<String>,
    val response: String
)

val AI_RESPONSES = listOf(

    AiResponse(
        keywords = listOf(
            "fever",
            "temperature",
            "hot",
            "warm",
            "bukhar"
        ),
        response = "If your baby has a temperature above 38°C (100.4°F), keep them hydrated with breast milk or formula. Sponge with lukewarm (not cold) water. Give paracetamol drops only as prescribed by your doctor. Seek immediate care if fever is above 39°C, baby is under 3 months, or has difficulty breathing, rash, or seizures."
    ),

    AiResponse(
        keywords = listOf(
            "feeding",
            "breastfeed",
            "breast milk",
            "formula",
            "nursing",
            "latch",
            "dudh",
            "feed"
        ),
        response = "Newborns need 8–12 feedings per day. Signs of good latch: baby's mouth covers both nipple and areola, no pain after initial seconds, you hear swallowing. If baby seems unsatisfied or cries after feeds, consult a lactation consultant. Formula-fed babies take about 60–90 ml per feed in the first month."
    ),

    AiResponse(
        keywords = listOf(
            "sleep",
            "not sleeping",
            "awake",
            "night",
            "nap",
            "so ja",
            "neend"
        ),
        response = "Newborns sleep 14–17 hours daily in short 2–4 hour cycles. Always place baby on their back (not tummy) to sleep. Establish a bedtime routine by 2–3 months: bath, massage, feed, sleep. Night wakings are normal until 6 months. Avoid sleep training before 4 months."
    ),

    AiResponse(
        keywords = listOf(
            "vaccine",
            "vaccination",
            "immunisation",
            "injection",
            "tika"
        ),
        response = "India's National Immunisation Schedule (NIS) starts at birth with BCG, OPV and Hepatitis B. Next vaccines are due at 6 weeks. After vaccination, mild fever and local redness are normal — apply a cold cloth and give paracetamol if prescribed. Never skip a vaccine; delayed vaccines can be given at the next visit."
    ),

    AiResponse(
        keywords = listOf(
            "vomit",
            "spit up",
            "regurgitate",
            "ulti"
        ),
        response = "Small spit-ups after feeds are very common (reflux) and usually harmless. Hold baby upright for 20–30 min after feeding, burp frequently. See a doctor if baby is projectile vomiting (forceful), losing weight, or vomiting is greenish."
    ),

    AiResponse(
        keywords = listOf(
            "colic",
            "crying",
            "cry",
            "inconsolable",
            "ro raha"
        ),
        response = "Colic is defined as crying for more than 3 hours a day for 3+ days a week in an otherwise healthy baby. Try swaddling, gentle rocking, white noise, or a warm bath. Colic usually peaks at 6 weeks and resolves by 3–4 months. Rule out hunger, gas, or illness first."
    ),

    AiResponse(
        keywords = listOf(
            "diaper",
            "rash",
            "nappy",
            "skin",
            "red bottom",
            "nappy rash"
        ),
        response = "Keep the nappy area clean and dry. Change diapers frequently. Apply zinc oxide cream at each change as a barrier. Let the skin air-dry for a few minutes before putting on a new nappy. If the rash is severe, has blisters, or doesn't improve in 3 days, see your doctor."
    ),

    AiResponse(
        keywords = listOf(
            "solid",
            "food",
            "wean",
            "eating",
            "start food",
            "khana",
            "complement"
        ),
        response = "Introduce solids at 6 months — not before. Start with single-ingredient purees: rice water, dal water, cooked and mashed vegetables (carrot, potato, pumpkin). Introduce one new food every 3 days to watch for allergies. Continue breastfeeding alongside solids until at least 2 years."
    ),

    AiResponse(
        keywords = listOf(
            "weight",
            "growth",
            "gaining",
            "underweight",
            "not growing"
        ),
        response = "Average birth weight is 2.5–4 kg. Babies lose up to 10% in the first week, then regain by day 10–14. Expected gain: 150–200 g/week in months 1–3, 100–150 g/week in months 3–6. Birthweight doubles by 5–6 months and triples by 12 months. Plot growth on the WHO chart at each visit."
    ),

    AiResponse(
        keywords = listOf(
            "cold",
            "cough",
            "congestion",
            "runny nose",
            "sneezing",
            "sardi",
            "khansi"
        ),
        response = "Colds are very common in babies. Use saline nasal drops and a nasal aspirator to clear congestion before feeds. Elevate the head of the cot slightly. Keep the baby well-hydrated. Seek care if breathing is rapid, noisy, or there are chest retractions, or the baby is not feeding."
    ),

    AiResponse(
        keywords = listOf(
            "diarrhoea",
            "loose stool",
            "watery stool",
            "dast",
            "potty"
        ),
        response = "In breastfed babies, loose and frequent stools are normal. Diarrhoea is watery, sudden, and a change from baseline. Give ORS (oral rehydration solution) and continue breastfeeding. Seek immediate care if there is blood in stool, the baby is less than 6 months, or signs of dehydration (sunken eyes, no wet nappy in 6+ hours)."
    ),

    AiResponse(
        keywords = listOf(
            "jaundice",
            "yellow",
            "yellowish",
            "pagal",
            "piliya"
        ),
        response = "Physiological jaundice appears in the first 2–3 days and resolves by 2 weeks. Place the baby in indirect sunlight (near a window) for 15–20 min twice daily. Ensure frequent feeding (8–12 times/day). Seek urgent care if jaundice appears before 24 hours, spreads to palms and soles, or the baby is very sleepy."
    )
)

fun getAiResponse(query: String): String {

    val lowerCaseQuery = query.lowercase().trim()

    val matchedResponse = AI_RESPONSES.firstOrNull { aiResponse ->

        aiResponse.keywords.any { keyword ->
            lowerCaseQuery.contains(keyword)
        }
    }

    return matchedResponse?.response
        ?: "Thank you for your question. For personalised advice about your baby, please consult your paediatrician or call the national health helpline at 1800-180-1104. I can answer questions about fever, feeding, sleep, vaccines, colic, diaper rash, solids, growth, colds, and jaundice."
}
