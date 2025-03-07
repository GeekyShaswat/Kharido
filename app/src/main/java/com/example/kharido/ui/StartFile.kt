package com.example.kharido.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import com.example.kharido.ViewModelClass.AuthViewModel
import com.example.kharido.ViewModelClass.ModelClass
import com.example.kharido.ui.theme.CartScreen
import com.example.kharido.ui.theme.CheckoutScreen
import com.example.kharido.ui.theme.LoginPage
import com.example.kharido.ui.theme.SignUpPage

enum class Names(val title: String) {
    Category("Welcome to Kharido"),
    SubCategory("Category"),
    Cart("Cart"),
    LoginIn("New Here ! Sign Up"),
    SignIn("Already a User ! Sign In"),
    CheckOut("Checkout")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Start(modelClass : ModelClass,
          authViewModel: AuthViewModel,
          navController : NavHostController = rememberNavController()
){
    var canNavigate = false
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Names.valueOf(
        backStackEntry?.destination?.route ?: Names.Category.name)
    var bottomBarVisibility by remember { mutableStateOf(true) }

    canNavigate = navController.previousBackStackEntry != null
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(end = 10.dp)){
                    Text(
                        text = currentScreen.title,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    if (currentScreen == Names.Category) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "cart",
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(25.dp)
                        )
                    }
                }

                },
                navigationIcon = {if (canNavigate){
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }

                }
            )
        },
        bottomBar = {
            AnimatedVisibility(bottomBarVisibility){
                Bottombar(navController,currentScreen)
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Names.LoginIn.name,
            modifier = Modifier.padding(innerPadding)
    )
    {
        composable(route = Names.CheckOut.name){
            CheckoutScreen(navController)
            bottomBarVisibility =false
        }
        composable(route = Names.Category.name) {
            Startpage(modelClass,
                onCategoryClick = {
                    modelClass.fetchSubcat()
                    modelClass.updateSelectedCategory(it)
                    Log.d("Navigation", "Navigating to SubCategory")
                    navController.navigate(Names.SubCategory.name)
                })
            bottomBarVisibility =true
        }
        composable(route = Names.SubCategory.name){
            CheckStatus(modelClass)
        }
        composable(route = Names.Cart.name){
            CartScreen(modelClass, onClick = {
                navController.navigate(Names.Category.name){
                    popUpTo(0)
                }
            },
                navController
            )
            bottomBarVisibility =true
        }
        composable(route = Names.LoginIn.name){
            LoginPage(Modifier,navController,authViewModel)
            bottomBarVisibility =false
        }
        composable(route = Names.SignIn.name){
            SignUpPage(Modifier,navController,authViewModel)
            bottomBarVisibility =false
        }
    }
    }
}

@Composable
fun Bottombar(
    navController : NavHostController,
    currentscreen : Names
){
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, end = 45.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.clickable{navController.navigate(Names.Category.name){
                    popUpTo(0)
                } }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "home",
                    modifier = Modifier
                        .size(25.dp)

                )
                Text(text = "Home", Modifier.padding(bottom = 3.dp))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable{
                    if(currentscreen != Names.Cart){ navController.navigate(Names.Cart.name)
                        Toast.makeText(context, "Cart icon clicked " , Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "cart",
                    modifier = Modifier
                        .size(25.dp)
                )
                Text(text = "Cart", Modifier.padding(bottom = 3.dp))
            }
        }
}