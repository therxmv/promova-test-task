package com.therxmv.promovatest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.therxmv.base.ui.theme.PromovaTheme
import com.therxmv.promovatest.ui.NavigationContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PromovaTheme {
                Surface {
                    NavigationContent()
                }
            }
        }
    }
}