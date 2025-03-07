package com.example.kharido.ui.theme

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kharido.ui.Names

@Composable
fun CheckoutScreen(navController: NavController){
    val context = LocalContext.current
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center){
        Text(
            text = "Payment Processed",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Thank you for your purchase!",
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
        Row(Modifier.fillMaxWidth().padding(40.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Card(modifier = Modifier.clickable{
                Toast.makeText(context , "Order is placed ", Toast.LENGTH_SHORT).show()
            },
                elevation = CardDefaults.cardElevation(5.dp),
                shape = CardDefaults.outlinedShape,
                colors = CardDefaults.cardColors(Color.LightGray)
            ){
                Text("Track Order", fontSize = 16.sp, modifier = Modifier.padding(4.dp), color = Color.Black)
            }
            Card(modifier = Modifier.clickable{
                navController.navigate(Names.Category.name){
                    popUpTo(0)
                }
            },
                elevation = CardDefaults.cardElevation(5.dp),
                shape = CardDefaults.outlinedShape,
                colors = CardDefaults.cardColors(Color.LightGray)
            ) {
                Text("Go Home", fontSize = 16.sp, modifier = Modifier.padding(4.dp), color = Color.Black)
            }
        }
    }
}