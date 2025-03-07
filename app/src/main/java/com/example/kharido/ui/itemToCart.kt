package com.example.kharido.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kharido.ViewModelClass.ModelClass
import com.example.kharido.data.CartItems
import com.example.kharido.ui.theme.primaryDark
import com.example.kharido.ui.theme.primaryLightHighContrast
import com.example.kharido.ui.theme.surfaceLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@Composable
fun CheckStatus(modelClass: ModelClass) {
    val loading by modelClass.loading.collectAsState()
    val error by modelClass.error.collectAsState()

    when {
        loading -> Loading()
        error != null -> ErrorScreen2(modelClass)
        else -> SubCart(modelClass)
    }
}

@Composable
fun SubCart(modelClass: ModelClass) {
    val kharidoUiState by modelClass.uiState.collectAsState()
    val cat = kharidoUiState.SelectedCategory
    val subCartData by modelClass.subCart.collectAsState()
    val filterItem = subCartData.filter { it.category == cat }

    Card(
        elevation = CardDefaults.cardElevation(7.dp),
        modifier = Modifier
            .padding(top = 16.dp, start = 7.dp, end = 7.dp, bottom = 20.dp)
            .clip(RoundedCornerShape(15.dp)),
        colors = CardDefaults.cardColors(surfaceLight)
    ) {
        Column {
            TopBar(cat)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier.padding(7.dp)
            ) {
                items(filterItem) { item ->
                    Layout2(
                        modelClass,
                        drawable = item.image,
                        price = item.price,
                        rating = item.rating,
                        name = cat
                    )
                }
            }
        }
    }
}

@Composable
fun Layout2(
    modelClass: ModelClass,
    drawable: String,
    price: Int,
    rating: Int,
    name: String
) {
    val discountedPrice = price - (price * 0.25)
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(12.dp)
            .clickable {
                Toast
                    .makeText(context, "Screen clicked", Toast.LENGTH_SHORT)
                    .show()
            },
        colors = CardDefaults.cardColors(primaryDark)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                AsyncImage(
                    model = drawable,
                    contentDescription = "clothes",
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .width(140.dp)
                        .height(120.dp)
                        .border(3.dp, Color.Black, RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop
                )

                Card(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 3.dp)
                        .align(Alignment.TopEnd),
                    colors = CardDefaults.cardColors(primaryLightHighContrast)
                ) {
                    Text(
                        "25%",
                        modifier = Modifier.padding(7.dp),
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }

            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "$price",
                        modifier = Modifier.padding(top = 8.dp, start = 13.dp),
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.LineThrough
                    )

                    Text(
                        "Rating",
                        modifier = Modifier.padding(top = 8.dp, end = 12.dp),
                        fontSize = 14.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "$discountedPrice",
                        modifier = Modifier.padding(start = 13.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "$rating/5",
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(end = 25.dp)
                            .align(Alignment.Bottom),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        CoroutineScope(Dispatchers.Main).launch{ modelClass.addToCart(CartItems(drawable, name, price, 1)) }
                        Toast
                            .makeText(context, "Item Added to Cart", Toast.LENGTH_SHORT)
                            .show()
                    }
            ) {
                Text(
                    "Add To Cart",
                    modifier = Modifier.padding(4.dp),
                    fontSize = 15.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TopBar(text: String) {
    Spacer(modifier = Modifier.padding(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun ErrorScreen2(modelClass: ModelClass) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = com.example.kharido.R.drawable.errorimage),
                contentDescription = "error",
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .height(200.dp)
                    .width(200.dp)
                    .border(3.dp, Color.Black, RoundedCornerShape(15.dp))
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Card(
                colors = CardDefaults.cardColors(Color.LightGray),
                onClick = {
                    Log.d("Error", "Retry button clicked")
                    modelClass.fetchSubcat()
                }
            ) {
                Text(
                    "RETRY",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(10.dp),
                    color = Color.Black
                )
            }
        }
    }
}
