package com.mariogarluu.dragonballapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mariogarluu.dragonballapi.ui.navigation.NavGraph
import com.mariogarluu.dragonballapi.ui.theme.DragonBallApiTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 * This activity is the entry point of the application and hosts the NavGraph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in [onSaveInstanceState].  <b><i>Note: Otherwise it is null.</i></b>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragonBallApiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                        NavGraph(
                            navController = navController,
                            contentPadding = paddingValues
                        )
                    }
                }
            }
        }
    }
}