package com.example.kharido.data
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.kharido.R
import com.example.kharido.data.SubCat

data class SubCat(
    val image : String,
    val category : String,
    val price : Int,
    val rating : Int
)

//        ).filter { it.categoryId == category }


