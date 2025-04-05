package uk.ac.tees.mad.tuneflow.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.model.dataclass.UserData
import uk.ac.tees.mad.tuneflow.model.dataclass.UserDetails
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository

class ProfileScreenViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    private val _updateNameResult = MutableStateFlow<AuthResult<Boolean>>(AuthResult.Success(false))
    val updateNameResult: StateFlow<AuthResult<Boolean>> = _updateNameResult.asStateFlow()

    private val _updatePhotoResult = MutableStateFlow<AuthResult<Boolean>>(AuthResult.Success(false))
    val updatePhotoResult: StateFlow<AuthResult<Boolean>> = _updatePhotoResult.asStateFlow()

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        viewModelScope.launch {
            authRepository.getCurrentUserDetails().collect { result ->
                _userDetails.value = result
                if (result is AuthResult.Success) {
                    _userData.update {
                        it.copy(
                            userDetails = result.data, userId = authRepository.getCurrentUserId()
                        )
                    }
                }
            }
        }
    }

    fun signOut() {
        authRepository.SignOut()
    }

    fun updateDisplayName(displayName: String) {
        viewModelScope.launch {
            authRepository.updateDisplayName(displayName).collect { result ->
                _updateNameResult.value = result
                if (result is AuthResult.Success) {
                    fetchUserDetails()
                }
            }
        }
    }

    fun updatePhoto(photoUri: Uri) {
        viewModelScope.launch {
            authRepository.updatePhoto(photoUri).collect { result ->
                _updatePhotoResult.value = result
                if (result is AuthResult.Success) {
                    fetchUserDetails()
                }
            }
        }
    }

}