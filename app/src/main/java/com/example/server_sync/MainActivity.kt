package com.example.server_sync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.server_sync.ui.theme.others.AppNavGraph
import com.example.server_sync.ui.theme.AptoideAppTheme
import com.example.server_sync.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val digitalKeyViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AptoideAppTheme {
                AppNavGraph(digitalKeyViewModel)
            }
        }
    }
}