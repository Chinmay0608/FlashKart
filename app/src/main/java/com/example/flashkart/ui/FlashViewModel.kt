package com.example.flashkart.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.flashkart.data.InternetItem
import com.example.flashkart.data.Item
import com.example.flashkart.network.FlashApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Job

class FlashViewModel: ViewModel(){
    private val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(FlashUiState())
    val uiState: StateFlow<FlashUiState> = _uiState.asStateFlow()

    private val _isVisible = MutableStateFlow<Boolean>(true)
    val isVisible = _isVisible

    var itemUiState: ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: MutableStateFlow<String> get() = _phoneNumber

    private val _cartItems = MutableStateFlow<List<Item>>(emptyList())
    val cartItems: StateFlow<List<Item>> get() = _cartItems.asStateFlow()

    private val _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    private val _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId

    private val _ticks = MutableStateFlow(60L)
    val ticks: MutableStateFlow<Long> get() = _ticks

    private val database = Firebase.database
    private var myRef: DatabaseReference? = null
    private var cartListener: ValueEventListener? = null

    private lateinit var timerJob: Job

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean>get()= _loading

    private val _logoutClicked = MutableStateFlow(false)
    val logoutClicked: MutableStateFlow<Boolean> get() = _logoutClicked

    private var screenJob: Job

    sealed interface ItemUiState {
        data class Success(val items: List<InternetItem>) : ItemUiState
        data object Loading : ItemUiState
        data object Error : ItemUiState
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setOtp(otp:String){
        _otp.value = otp
    }

    fun setVerificationId(verificationId:String){
        _verificationId.value = verificationId
    }

    fun setUser(user: FirebaseUser?){
        if (_user.value?.uid != user?.uid) {
            _user.value = user
            if (user != null) {
                setupCartListener(user.uid)
            } else {
                removeCartListener()
            }
        }
    }

    private fun setupCartListener(uid: String) {
        removeCartListener()
        val newRef = database.getReference("users/$uid/cart")
        myRef = newRef
        cartListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("CART", "Cart data changed! Snapshot has ${dataSnapshot.childrenCount} children")
                val items = mutableListOf<Item>()
                for (childSnapshot in dataSnapshot.children){
                    try {
                        val item = childSnapshot.getValue(Item::class.java)
                        item?.let { items.add(it) }
                    } catch (e: Exception) {
                        Log.e("CART", "Failed to parse item", e)
                    }
                }
                _cartItems.value = items
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CART", "DatabaseError: ${error.message}", error.toException())
            }
        }
        newRef.addValueEventListener(cartListener!!)
    }

    private fun removeCartListener() {
        cartListener?.let { myRef?.removeEventListener(it) }
        myRef = null
        cartListener = null
        _cartItems.value = emptyList()
    }

    fun clearData(){
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
        removeCartListener()
        resetTimer()
    }

    fun runTimer(){
        timerJob = viewModelScope.launch{
            while(_ticks.value > 0){
                delay(1000)
                _ticks.value -=1
            }
        }
    }

    fun setLoading(isLoading:Boolean){
        _loading.value = isLoading
    }

    fun setLogoutStatus(logoutStatus: Boolean){
        _logoutClicked.value = logoutStatus
    }

    fun resetTimer(){
        try {
            timerJob.cancel()
        } catch (_:Exception){

        }finally {
            _ticks.value = 60
        }
    }

    fun addToCart(item: Item) {
        Log.d("CART", "AddToCart called: ${item.itemName}")
        if (myRef == null) {
            Log.e("CART", "Failed to add: User not logged in or database reference null")
            return
        }
        myRef?.push()?.setValue(item)
            ?.addOnSuccessListener {
                Log.d("CART", "Successfully added to cart: ${item.itemName}")
            }
            ?.addOnFailureListener { e ->
                Log.e("CART", "Failed to add to cart: ${e.message}", e)
            }
    }

    fun removeFromCart(oldItem: Item) {
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    var itemRemoved = false
                    val item = childSnapshot.getValue(Item::class.java)
                    item?.let {
                        if (oldItem.itemName == it.itemName &&
                            oldItem.itemQuantityId == it.itemQuantityId
                        ) {
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }
                    }
                    if(itemRemoved) break
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CART", "DatabaseError: ${error.message}", error.toException())
            }
        })
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

    private fun toggleVisibility() {
        _isVisible.value = false
    }

    fun getFlashItems() {
        itemUiState = ItemUiState.Loading

        viewModelScope.launch {
            try {
                val items = FlashApi.retrofitService.getItems()

                itemUiState = ItemUiState.Success(items)

            } catch (e: Exception) {
                itemUiState = ItemUiState.Error
                toggleVisibility()
                screenJob.cancel()
            }
        }
    }

    init {
        try {
            Firebase.database.setPersistenceEnabled(true)
        } catch (e: Exception) {
            Log.e("CART", "Persistence already enabled")
        }
        
        screenJob = viewModelScope.launch(Dispatchers.Default) {
            delay(3000)
            toggleVisibility()
        }
        getFlashItems()
        auth.currentUser?.let { setUser(it) }
    }
}