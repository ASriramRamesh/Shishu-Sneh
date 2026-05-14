package com.shishusneh.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shishusneh.app.ui.navigation.AppNavigation
import com.shishusneh.app.ui.theme.ShishuSnehTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShishuSnehTheme {
                AppNavigation()
            }
        }
    }
}
