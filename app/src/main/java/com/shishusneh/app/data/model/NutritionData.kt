package com.shishusneh.app.data.model

data class NutritionTip(
    val title: String,
    val body: String
)

data class NutritionStage(
    val ageRange: String,
    val description: String,
    val foods: List<String>
)

val DAILY_TIPS = listOf(

    NutritionTip(
        "Breast is best",
        "Exclusive breastfeeding for the first 6 months provides all the nutrition your baby needs — no water, no other food."
    ),

    NutritionTip(
        "Feed on demand",
        "Let your baby guide feeding frequency. Newborns feed 8–12 times a day; this stimulates milk supply."
    ),

    NutritionTip(
        "Skin-to-skin contact",
        "Holding your baby skin-to-skin in the first hour after birth helps initiate breastfeeding and bonding."
    ),

    NutritionTip(
        "Burp after every feed",
        "Burp your baby by holding them upright and gently patting the back. This reduces gas and spit-ups."
    ),

    NutritionTip(
        "Watch for hunger cues",
        "Rooting, sucking fists, and turning the head are early hunger cues. Crying is a late cue — aim to feed before that."
    ),

    NutritionTip(
        "Start solids at 6 months",
        "Before 6 months, the gut is not ready for solids. After 6 months, start with single-ingredient smooth purees."
    ),

    NutritionTip(
        "Iron-rich foods",
        "After 6 months, include iron-rich foods like pureed meat, dal, and fortified cereals — breastfed babies need extra iron."
    ),

    NutritionTip(
        "Traditional Indian foods work well",
        "Khichdi, moong dal water, ragi porridge, and mashed banana are excellent first foods in the Indian context."
    )
)

val NUTRITION_STAGES = listOf(

    NutritionStage(
        ageRange = "0–6 months",
        description = "Exclusive breastfeeding — breast milk or formula provides 100% of nutritional needs.",
        foods = listOf(
            "Breast milk",
            "Formula (if needed)",
            "No water, no juice, no solids"
        )
    ),

    NutritionStage(
        ageRange = "6–8 months",
        description = "Introduce single-ingredient smooth purees one at a time. Wait 3 days before introducing a new food.",
        foods = listOf(
            "Rice kanji / dal water",
            "Mashed banana",
            "Pureed sweet potato / carrot / pumpkin",
            "Apple / pear puree",
            "Moong dal soup",
            "Ragi porridge (thin)",
            "Pureed chicken or fish (well-cooked)"
        )
    ),

    NutritionStage(
        ageRange = "8–10 months",
        description = "Progress to thicker textures, mashed foods, and introduce a wider variety.",
        foods = listOf(
            "Khichdi (soft)",
            "Mashed vegetables",
            "Curd / dahi",
            "Soft-cooked egg yolk",
            "Mashed ripe papaya / mango",
            "Paneer (soft)",
            "Thick dal"
        )
    ),

    NutritionStage(
        ageRange = "10–12 months",
        description = "Move towards family foods with soft textures. Introduce finger foods to encourage self-feeding.",
        foods = listOf(
            "Soft chapati pieces",
            "Small banana slices",
            "Soft-cooked vegetable cubes",
            "Cheese cubes",
            "Rice with dal",
            "Scrambled eggs",
            "Idli / dosa (soft)",
            "Well-cooked fish pieces"
        )
    )
)

val BREASTFEEDING_TIPS = listOf(

    "Ensure a good latch — baby's mouth should cover the areola, not just the nipple.",

    "Feed from both breasts at each session to maintain supply.",

    "Drink plenty of water — breastfeeding mothers need extra fluids.",

    "Eat a nutritious diet with lentils, green vegetables, and dairy.",

    "Avoid caffeine and alcohol; both pass into breast milk.",

    "If milk supply seems low, try feeding more frequently — supply follows demand.",

    "Store expressed breast milk in the fridge for up to 4 days, or freeze for up to 6 months.",

    "Cracked nipples: apply breast milk after feeding and let air-dry. Seek help if very painful."
)
