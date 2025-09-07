package com.therxmv.base.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalPromovaTheme = staticCompositionLocalOf { PromovaTheme }

object PromovaTheme {

    val paddings = Paddings()

    data class Paddings(
        val main: Dp = 12.dp,
        val halfOfMain: Dp = main / 2,
    )
}