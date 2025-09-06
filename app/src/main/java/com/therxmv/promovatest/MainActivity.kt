package com.therxmv.promovatest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.therxmv.base.ui.theme.PromovaTheme
import com.therxmv.featuremovies.ui.content.MoviesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PromovaTheme {
                MoviesScreen()
            }
        }
    }
}