package com.example.kharido.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kharido.ViewModelClass.ModelClass
import com.example.kharido.R
import com.example.kharido.data.CartItems
import com.example.kharido.ui.Names
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent


@Composable
fun CartScreen(
    modelClass: ModelClass,
    onClick:() -> Unit,
    navController : NavController
    ){
    val cartItem = modelClass.cartItems.collectAsState().value
    if(cartItem.isNotEmpty()){
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Image(
                    painter = painterResource(R.drawable.offer),
                    "Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp)
                )
            }
            item {
                Text(
                    text = "Items in Cart",
                    modifier = Modifier.padding(top = 8.dp, start = 3.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
            }
            items(cartItem) {
                ItemLayout(it,modelClass)
            }
            val totalAmount  = (cartItem.sumOf { it.price * it.quantity }).toFloat()
            val discount = totalAmount * 0.25
            item {
                TextBill("Sub Total : ", totalAmount.toString(), FontWeight.Bold)
                TextBill("Discount : ", discount.toString())
                TextBill("Total : ", (totalAmount - discount).toString(), FontWeight.Bold)
                Button(onClick = { navController.navigate(Names.CheckOut.name) },modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    Text(text = "Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
    else EmptyCart(onClick )

}

@Composable
fun EmptyCart(onClick:() -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(color = Color.LightGray)){
        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = painterResource(R.drawable.emptycart),
                "Image",
                modifier = Modifier.size(250.dp)
            )
            Text(text = "Your Cart is Empty", fontSize = 20.sp, modifier = Modifier.padding(8.dp), color = Color.Red)
            Button(onClick = onClick) {
                Text(text = "Continue Shopping", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ItemLayout(cartItems: CartItems,
               modelClass: ModelClass
){
    Card(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).padding(6.dp, bottom = 3.dp)){
        Row {
            AsyncImage(model = cartItems.image,
                contentDescription = "clothes",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .height(100.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .weight(3f)
                    .border(
                        width = 3.dp,  // Border thickness
                        color = Color.Black,
                        shape = RoundedCornerShape(15.dp)// Border color
                    )
            )
            Column(modifier = Modifier.weight(2f).padding(start = 6.dp)) {
                Text(text = cartItems.name , fontSize = 15.sp )
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                Text(text = cartItems.price.toString() , fontSize = 15.sp)
            }
            Column(modifier = Modifier.weight(3f)) {
                Text(text = "Quantity: ${cartItems.quantity}" , fontSize = 15.sp )
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                Button(onClick = {
                    CoroutineScope(Dispatchers.Main).launch{
                        modelClass.removeFromCart(cartItems)
                    } })
                {
                    Text(text = "Remove")
                }
            }
        }
    }

}
@Composable
fun TextBill(
    text : String = "abs",
    amount : String= "1234",
    weight : FontWeight = FontWeight.Normal
    ){
    Card(colors = CardDefaults.cardColors(Color.LightGray), modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
        Row(modifier = Modifier.padding(7.dp)){
            Text(text = text , fontSize = 14.sp , modifier = Modifier.padding(start = 5.dp), fontWeight = weight,color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = amount , fontSize = 14.sp , modifier = Modifier.padding(start = 5.dp,end = 8.dp), fontWeight = weight,color = Color.Black)
        }

    }
}