package com.example.kharido.data

import kotlinx.serialization.Serializable


@Serializable
data class CartItems (
    val image : String,
    val name : String,
    val price : Int,
    val quantity : Int
)
