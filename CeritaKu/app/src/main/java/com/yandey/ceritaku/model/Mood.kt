package com.yandey.ceritaku.model

import androidx.compose.ui.graphics.Color
import com.yandey.ceritaku.ui.theme.AngryColor
import com.yandey.ceritaku.ui.theme.AwfulColor
import com.yandey.ceritaku.ui.theme.BoredColor
import com.yandey.ceritaku.ui.theme.CalmColor
import com.yandey.ceritaku.ui.theme.DepressedColor
import com.yandey.ceritaku.ui.theme.DisappointedColor
import com.yandey.ceritaku.ui.theme.HappyColor
import com.yandey.ceritaku.ui.theme.HumorousColor
import com.yandey.ceritaku.ui.theme.LonelyColor
import com.yandey.ceritaku.ui.theme.MysteriousColor
import com.yandey.ceritaku.ui.theme.NeutralColor
import com.yandey.ceritaku.ui.theme.RomanticColor
import com.yandey.ceritaku.ui.theme.ShamefulColor
import com.yandey.ceritaku.ui.theme.SurprisedColor
import com.yandey.ceritaku.ui.theme.SuspiciousColor
import com.yandey.ceritaku.ui.theme.TenseColor
import com.yandey.deardiary.R

enum class Mood(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color,
) {
    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = HappyColor
    ),
    Romantic(
        icon = R.drawable.romantic,
        contentColor = Color.White,
        containerColor = RomanticColor
    ),
    Calm(
        icon = R.drawable.calm,
        contentColor = Color.Black,
        containerColor = CalmColor
    ),
    Tense(
        icon = R.drawable.tense,
        contentColor = Color.Black,
        containerColor = TenseColor
    ),
    Lonely(
        icon = R.drawable.lonely,
        contentColor = Color.White,
        containerColor = LonelyColor
    ),
    Mysterious(
        icon = R.drawable.mysterious,
        contentColor = Color.Black,
        containerColor = MysteriousColor
    ),
    Angry(
        icon = R.drawable.angry,
        contentColor = Color.White,
        containerColor = AngryColor
    ),
    Awful(
        icon = R.drawable.awful,
        contentColor = Color.Black,
        containerColor = AwfulColor
    ),
    Surprised(
        icon = R.drawable.surprised,
        contentColor = Color.Black,
        containerColor = SurprisedColor
    ),
    Depressed(
        icon = R.drawable.depressed,
        contentColor = Color.Black,
        containerColor = DepressedColor
    ),
    Disappointed(
        icon = R.drawable.disappointed,
        contentColor = Color.White,
        containerColor = DisappointedColor
    ),
    Shameful(
        icon = R.drawable.shameful,
        contentColor = Color.White,
        containerColor = ShamefulColor
    ),
    Humorous(
        icon = R.drawable.humorous,
        contentColor = Color.Black,
        containerColor = HumorousColor
    ),
    Suspicious(
        icon = R.drawable.suspicious,
        contentColor = Color.Black,
        containerColor = SuspiciousColor
    ),
    Bored(
        icon = R.drawable.bored,
        contentColor = Color.Black,
        containerColor = BoredColor
    )
}