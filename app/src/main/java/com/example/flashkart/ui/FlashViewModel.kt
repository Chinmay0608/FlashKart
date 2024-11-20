package com.example.flashkart.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.flashkart.network.FlashApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import com.example.flashkart.data.InternetItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FlashViewModel(application: Application):AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(FlashUiState())
    val uiState: StateFlow<FlashUiState> = _uiState.asStateFlow()

    val _isVisible = MutableStateFlow<Boolean>(true)
    val isVisible = _isVisible

    var itemUiState: ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    private val _cartItems = MutableStateFlow<List<InternetItem>>(emptyList())
    val cartItems: StateFlow<List<InternetItem>> get() = _cartItems.asStateFlow()

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "cart")
    private val cartItemsKey = stringPreferencesKey("cartItems")

//    private val _loading = MutableStateFlow(false)
//    val loading: MutableStateFlow<Boolean>get()= _loading

    private val context = application.applicationContext

    lateinit var internetJob: Job
    lateinit var screenJob: Job

    sealed interface ItemUiState {
        data class Success(val items: List<InternetItem>) : ItemUiState
        object Loading : ItemUiState
        object Error : ItemUiState
    }

    private suspend fun saveCartItemsToDataStore() {
        context.datastore.edit { preferences ->
            preferences[cartItemsKey] = Json.encodeToString(_cartItems.value)
        }
    }

    private suspend fun loadCartItemsFromDataStore() {
        val fullData = context.datastore.data.first()
        val cartItemsJson = fullData[cartItemsKey]
        if (!cartItemsJson.isNullOrEmpty()) {
            _cartItems.value = Json.decodeFromString(cartItemsJson)
        }
    }

    fun addToCart(item: InternetItem) {
        _cartItems.value = _cartItems.value + item
        viewModelScope.launch {
            saveCartItemsToDataStore()
        }
    }

    fun removeFromCart(item: InternetItem) {
        _cartItems.value = _cartItems.value - item
        viewModelScope.launch {
            saveCartItemsToDataStore()
        }
    }

    fun updateClickText(updatedText: String) {
        _uiState.update {
            it.copy(
                clickStatus = updatedText
            )
        }
    }

        fun updateSelectedCategory(updatedCategory: Int) {
            _uiState.update {
                it.copy(
                    selectedCategory = updatedCategory
                )
            }
        }

        fun toggleVisibility() {
            _isVisible.value = false
        }

        fun getFlashItems() {
            internetJob = viewModelScope.launch {
                try {
                    val listResult = FlashApi.retrofitService.getItems()
                    itemUiState = ItemUiState.Success(listResult)
                    loadCartItemsFromDataStore()
                } catch (exception: Exception) {
                    itemUiState = ItemUiState.Error
                    toggleVisibility()
                    screenJob.cancel()
                }
            }
        }

        init {
            screenJob = viewModelScope.launch(Dispatchers.Default) {
                delay(3000)
                toggleVisibility()
            }
            getFlashItems()
        }
    }