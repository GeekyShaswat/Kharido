package com.example.kharido.ViewModelClass

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kharido.Internet.RetrofitInstance
import com.example.kharido.data.CartItems
import com.example.kharido.data.SubCat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ModelClass(application : Application ): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(KharidoUiState())
    val uiState : StateFlow<KharidoUiState> = _uiState

    private val _category = MutableStateFlow<List<Category>>(emptyList())
    val category : StateFlow<List<Category>> = _category

    private var _verticalImages = MutableStateFlow<List<Category>>(emptyList())
    val verticalImages : StateFlow<List<Category>> = _verticalImages

    private val _subCart = MutableStateFlow<List<SubCat>>(emptyList())
    val subCart : StateFlow<List<SubCat>> = _subCart

    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> = _error

    private val _cartItems = MutableStateFlow<List<CartItems>>(emptyList())
    val cartItems : StateFlow<List<CartItems>> = _cartItems

    private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "cart")
    private val context = application.applicationContext
    private val cartItemsKey = stringPreferencesKey("cart_Items")

    suspend fun SaveCartItems(cartItems: List<CartItems>){
        context.dataStore.edit { preferences ->
            preferences[cartItemsKey]= Json.encodeToString(cartItems)
        }

    }

    private suspend fun loadCartItems() {
        val fullData = context.dataStore.data.first()
        val cartItemsString = fullData[cartItemsKey]
        if (!cartItemsString.isNullOrEmpty()) {
            try {
                _cartItems.value = Json.decodeFromString(cartItemsString)
            } catch (e: Exception) {
                Log.e("loadCartItems", "Failed to parse cart items: ${e.message}")
            }
        }
    }

    suspend fun addToCart(item: CartItems) {
        val currentList = _cartItems.value.toMutableList()
        val existingItemIndex = currentList.indexOfFirst { it.image == item.image }

        if (existingItemIndex != -1) {
            val existingItem = currentList[existingItemIndex]
            currentList[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentList.add(item)
        }

        SaveCartItems(currentList)
        loadCartItems()
        _cartItems.value = currentList
        Log.d("item", "$_cartItems")
    }

    suspend fun removeFromCart(item: CartItems) {
        val currentList = _cartItems.value.toMutableList()
        val itemIndex = currentList.indexOfFirst { it.image == item.image }

        if (itemIndex != -1) {
            val existingItem = currentList[itemIndex]
            if (existingItem.quantity > 1) {
                currentList[itemIndex] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentList.removeAt(itemIndex)
            }
        }

        SaveCartItems(currentList)
        loadCartItems()
        _cartItems.value = currentList
        Log.d("item", "${_cartItems.value}")
    }


    init {
        fetchCategory()
    }
    fun fetchSubcat(){
        viewModelScope.launch{
            _loading.value = true
            var success = false
            var attempt = 0
            val maxAttempt = 3
            while(attempt < maxAttempt && !success){
                try {
                    val response = RetrofitInstance.api3.getSubItem()
                    _subCart.value = response
                    Log.d("API", "values are :${response}")
                    success = true
                    _error.value = null
                } catch (e: Exception) {
                    attempt++
                    _error.value = "Error Occurred"
                    Log.e("Error", "error is ${e.localizedMessage}")
                    if (attempt >= maxAttempt) {
                        _error.value = "Error occurred"
                    } else {
                        delay(1000L * attempt)
                    }

                } finally {
                    if (attempt >= maxAttempt || success) {
                        _loading.value = false
                    }
                }
            }
        }
    }

    fun fetchCategory(){
        viewModelScope.launch{
            _loading.value = true
            var success = false
            var attempt = 0
            val maxAttempt = 3
            while(attempt < maxAttempt && !success){
                try {
                    val response = RetrofitInstance.api.getStartPage()
                    Log.d("API", "getStartPage Response: $response")

                    val response2 = RetrofitInstance.api2.getVerticalImages()
                    Log.d("API", "getVerticalImages Response: $response2")

                    _verticalImages.value = response2
                    _category.value = response
                    success = true
                    _error.value = null
                } catch (e: Exception) {
                    attempt++
                    _error.value = "Error Occurred"
                    Log.e("Error", "error is ${e.localizedMessage}")
                    if(attempt >= maxAttempt){
                        _error.value = "Error occurred"
                    }
                    else{
                        delay(1000L * attempt)
                    }
                } finally {
                    if(attempt >= maxAttempt || success){ _loading.value = false }
                }
            }
        }
    }


    fun updateSelectedCategory(updateCategory : String){
        _uiState.update{currentState ->
            currentState.copy(SelectedCategory = updateCategory)
        }
    }
}