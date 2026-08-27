package com.ajlr.lector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ajlr.lector.ui.MainScreen
import com.ajlr.lector.ui.ObraViewModel
import com.ajlr.lector.ui.theme.LectorTheme

class MainActivity : ComponentActivity() {
    private val obraViewModel: ObraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen(viewModel = obraViewModel)
                }
            }
        }
    }
}