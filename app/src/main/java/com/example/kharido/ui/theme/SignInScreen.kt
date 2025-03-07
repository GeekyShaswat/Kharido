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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kharido.ViewModelClass.AuthState
import com.example.kharido.ViewModelClass.AuthViewModel
import com.example.kharido.ui.Names

@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            AuthState.Authenticated -> {
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
        Text(text = "SignUp Page", fontSize = 30.sp)

        Spacer(modifier = Modifier.padding(15.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {it -> name = it},
            label = {Text("Name")}
        )

        Spacer(modifier = Modifier.padding(4.dp))

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
                authViewModel.signUp(email , name , password)
                if(authState.value == AuthState.Authenticated){
                    Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show()
                    navController.navigate(Names.LoginIn.name){
                        popUpTo(Names.LoginIn.name) { inclusive = true }
                    }
                }
            }
        ){
            Text(text = "Create Account")
        }

        Spacer(modifier = Modifier.padding(1.dp))

        androidx.compose.material3.TextButton(onClick = {
            navController.navigate(Names.LoginIn.name){
                popUpTo(Names.LoginIn.name) { inclusive = true }
            }
        }) {
            Text( text = "Already have an account!, Login ")
        }
    }

}