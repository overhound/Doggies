package com.ot.doggies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ot.doggies.navigation.MainNavigation
import com.ot.doggies.ui.theme.DoggiesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoggiesTheme {
                DoggiesApp()
            }
        }
    }

    @Composable
    fun DoggiesApp(navController: NavHostController = rememberNavController()) {
        Box(
            modifier = Modifier
                .padding(vertical = 48.dp)
                .fillMaxSize()
        ) {
            MainNavigation(navController)
        }
    }
}


