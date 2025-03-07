package com.example.kharido.Internet

import com.example.kharido.ViewModelClass.Category
import com.example.kharido.data.SubCat
import retrofit2.http.GET

interface ApiInterface {
    @GET("InternetTesting/main/horizontalList.json")
    suspend fun getStartPage() : List<Category>

}
interface Api2{
    @GET("InternetTesting/main/verticalList.json")
    suspend fun getVerticalImages() : List<Category>
}
interface Api3{
    @GET("subCart/main/subItem.json")
    suspend fun getSubItem() : List<SubCat>
}