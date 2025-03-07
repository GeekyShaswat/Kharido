package com.example.kharido.ViewModelClass


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser == null){
            _authState.value = AuthState.UnAuthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email :String , password : String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and Password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email , password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    _authState.value = AuthState.LoggedIn
                }else{
                    _authState.value = AuthState.Error(it.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    fun signUp(email : String , name : String , password : String){
        if(email.isEmpty() || name.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email , Name and Password cannot be empty")
        }

        _authState.value = AuthState.Loading

        if(auth.currentUser != null){
            auth.signOut()
        }

        auth.createUserWithEmailAndPassword(email , password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }
                else{
                    _authState.value = AuthState.Error(it.exception?.message ?: "Something Went Wrong")
                }
            }
    }
    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
}

sealed class AuthState{
    object LoggedIn : AuthState()
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}