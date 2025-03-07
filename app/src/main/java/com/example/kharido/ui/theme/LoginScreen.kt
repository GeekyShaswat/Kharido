package com.example.kharido.ui.theme


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kharido.ViewModelClass.AuthState
import com.example.kharido.ViewModelClass.AuthViewModel
import com.example.kharido.ui.Names
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    var context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            AuthState.LoggedIn -> {
                navController.navigate(Names.Category.name){
                    popUpTo(Names.LoginIn.name) { inclusive = true }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> null
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login Page", fontSize = 30.sp)

        Spacer(modifier = Modifier.padding(15.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {it -> email = it},
            label = {Text("Email")}
        )

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {it -> password = it},
            label = {Text("Password")},
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.padding(6.dp))

        Button(
            onClick = {
                authViewModel.login(email,password)
                if(authState.value == AuthState.Authenticated)navController.navigate(Names.Category.name){
                    popUpTo(Names.LoginIn.name) { inclusive = true }
                }

            }
        ){
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.padding(1.dp))

        androidx.compose.material3.TextButton(onClick = {
            navController.navigate(Names.SignIn.name){
                popUpTo(0)
            }
        }) {
            Text( text = "Don't have an Account ! , signUp ")
        }
    }
}