package com.shishusneh.app.data.model

data class Milestone(
    val id: String,
    val title: String,
    val description: String,
    val ageMonths: Int,
    val category: MilestoneCategory
)

enum class MilestoneCategory {
    MOTOR,
    COGNITIVE,
    SOCIAL,
    LANGUAGE,
    SENSORY
}

val ALL_MILESTONES = listOf(

    Milestone(
        "m1",
        "Lifts head",
        "During tummy time, baby can briefly lift head",
        1,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m2",
        "Follows moving objects",
        "Eyes track a moving face or toy",
        1,
        MilestoneCategory.SENSORY
    ),

    Milestone(
        "m3",
        "Social smile",
        "Smiles in response to your smile",
        2,
        MilestoneCategory.SOCIAL
    ),

    Milestone(
        "m4",
        "Coos and makes sounds",
        "Produces cooing and gurgling sounds",
        2,
        MilestoneCategory.LANGUAGE
    ),

    Milestone(
        "m5",
        "Holds head steady",
        "Can hold head steady when held upright",
        3,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m6",
        "Reaches for objects",
        "Reaches out for objects nearby",
        3,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m7",
        "Laughs out loud",
        "Produces clear laughter",
        4,
        MilestoneCategory.SOCIAL
    ),

    Milestone(
        "m8",
        "Rolls tummy to back",
        "Rolls from tummy position to back",
        4,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m9",
        "Recognises familiar faces",
        "Shows recognition of parents and caregivers",
        4,
        MilestoneCategory.COGNITIVE
    ),

    Milestone(
        "m10",
        "Grasps objects",
        "Holds a rattle or small toy placed in hand",
        4,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m11",
        "Babbling begins",
        "Produces chains of syllables (ba-ba, ma-ma)",
        5,
        MilestoneCategory.LANGUAGE
    ),

    Milestone(
        "m12",
        "Sits with support",
        "Can sit upright when supported",
        5,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m13",
        "Rolls back to tummy",
        "Rolls both ways",
        5,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m14",
        "Transfers objects hand to hand",
        "Passes toy from one hand to the other",
        6,
        MilestoneCategory.COGNITIVE
    ),

    Milestone(
        "m15",
        "Stranger anxiety",
        "Shows wariness around unfamiliar people",
        6,
        MilestoneCategory.SOCIAL
    ),

    Milestone(
        "m16",
        "Sits without support",
        "Sits stably without any help",
        7,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m17",
        "Pincer grasp",
        "Picks up small objects with finger and thumb",
        8,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m18",
        "Pulls to standing",
        "Pulls up on furniture to stand",
        8,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m19",
        "Waves bye-bye",
        "Waves hand as a social gesture",
        9,
        MilestoneCategory.SOCIAL
    ),

    Milestone(
        "m20",
        "Responds to name",
        "Turns when name is called",
        9,
        MilestoneCategory.LANGUAGE
    ),

    Milestone(
        "m21",
        "Cruising",
        "Walks along furniture holding for support",
        10,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m22",
        "First words",
        "Says 1–2 words meaningfully (mama, dada, pani)",
        10,
        MilestoneCategory.LANGUAGE
    ),

    Milestone(
        "m23",
        "Object permanence",
        "Understands hidden objects still exist",
        10,
        MilestoneCategory.COGNITIVE
    ),

    Milestone(
        "m24",
        "Claps hands",
        "Brings hands together in a clap",
        10,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m25",
        "Stands alone",
        "Stands without holding on for a few seconds",
        11,
        MilestoneCategory.MOTOR
    ),

    Milestone(
        "m26",
        "First steps",
        "Takes first independent steps",
        12,
        MilestoneCategory.MOTOR
    )
)
