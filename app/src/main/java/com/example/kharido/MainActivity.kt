package com.example.kharido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kharido.ViewModelClass.AuthViewModel
import com.example.kharido.ViewModelClass.ModelClass
import com.example.kharido.ui.Start
import com.example.kharido.ui.theme.KharidoTheme
class MainActivity : ComponentActivity() {
    private lateinit var modelClass: ModelClass
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelClass = ModelClass(application)
        authViewModel = AuthViewModel()
        enableEdgeToEdge()
        setContent {
            KharidoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Start(modelClass, authViewModel)
                }
            }
        }
    }
}
