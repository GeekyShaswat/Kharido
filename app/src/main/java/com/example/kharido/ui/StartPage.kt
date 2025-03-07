package com.example.kharido.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kharido.R
import com.example.kharido.ViewModelClass.ModelClass
import com.example.kharido.ui.theme.primaryLight
import com.example.kharido.ui.theme.surfaceLight



@Composable
fun Startpage(modelClass : ModelClass , onCategoryClick: (String) -> Unit) {
    val loading = modelClass.loading.collectAsState().value
    val error = modelClass.error.collectAsState().value

    when {
        loading -> {
            Loading()
        }

        error != null -> {
            ErrorScreen(modelClass)
        }

        else -> {  MainLayout(modelClass , onCategoryClick)}
        }
}

@Composable
fun MainLayout(modelClass : ModelClass,
              onCategoryClick : (String) -> Unit){
    val context = LocalContext.current;
    val category = modelClass.category.collectAsState().value
    val verticalItems = modelClass.verticalImages.collectAsState().value
    Column {
        Card(elevation = CardDefaults.cardElevation(7.dp),
            modifier = Modifier.padding(7.dp),
            colors = CardDefaults.cardColors(surfaceLight)
            ){
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(category){
                    Layout(
                        image = it.image,
                        text = it.name,
                        modelClass,
                        context,
                        onCategoryClick = {onCategoryClick(it)}
                    )
                }
            }
        }
        Card(elevation = CardDefaults.cardElevation(7.dp),
            modifier = Modifier.padding( top = 16.dp, start = 7.dp, end = 7.dp, bottom = 20.dp),
            colors = CardDefaults.cardColors(surfaceLight)
            ){
            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                contentPadding = PaddingValues(3.dp),
                modifier = Modifier.padding(4.dp),
            ) {
                items(verticalItems){
                    Layout(
                        image = it.image,
                        text = it.name,
                        modelClass,
                        context,
                        onCategoryClick = {onCategoryClick(it)}
                    )
                }
            }
        }
    }
}



@Composable
fun Layout(
    image: String,
    text: String,
    modelClass: ModelClass,
    context: Context,
    fontSize: Int = 12,
    onCategoryClick: (String) -> Unit,
    height: Int = 120,
    width: Int = 140,
    start: Int = 12,
    end: Int = 12,
    bottom: Int =7
){
    val categoryname = text
    Card(
        modifier = Modifier.padding(7.dp),
        elevation =  CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(primaryLight),
        onClick = {
            Toast.makeText(context, "You have Clicked the $categoryname card", Toast.LENGTH_SHORT).show()
            if ( categoryname == "SmartPhones" || categoryname == "Laptops" || categoryname == "Computer" || categoryname == "TV" || categoryname == "Hair Dryer" || categoryname == "Smart Watch" || categoryname == "Camera"){onCategoryClick("Electronics") }
            else if ( categoryname == "T-Shirt" || categoryname == "Bottle" || categoryname == "Carry Bag"   ){onCategoryClick("Clothes")}
            else if ( categoryname == "Racquet" || categoryname == "Football" ){onCategoryClick("Sports")}
            else if ( categoryname == "Beverage"  ){onCategoryClick("Food")}
            else onCategoryClick(text)
        }
    ) {
        val data = modelClass.category.collectAsState().value
        val data2 = modelClass.verticalImages.collectAsState().value
        if(!data.isEmpty() && !data2.isEmpty() ){
            AsyncImage(
                image, "image", modifier = Modifier
                    .padding(top = 12.dp, start = start.dp, end = end.dp, bottom = bottom.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .width(width.dp)
                    .height(height.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(
                        width = 3.dp,  // Border thickness
                        color = Color.Black,
                        shape = RoundedCornerShape(15.dp)
                    ),
                contentScale = ContentScale.Crop

            )

            Text(
                text = text,
                fontSize = fontSize.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 5.dp)
            )
        }
    }
}


@Composable
fun TopBarLayout(){
    Spacer(modifier = Modifier.padding(16.dp))
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector =Icons.Filled.ShoppingCart, contentDescription = "cart" ,modifier = Modifier
            .size(40.dp)
            .padding(end = 7.dp))
        Text(
            text = "Kharido",
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
        )
    }
    Spacer(modifier = Modifier.padding(bottom = 8.dp))
}





