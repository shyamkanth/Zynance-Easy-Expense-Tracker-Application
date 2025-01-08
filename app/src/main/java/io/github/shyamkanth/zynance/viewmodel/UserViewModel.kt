package io.github.shyamkanth.zynance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shyamkanth.zynance.modal.entity.User
import io.github.shyamkanth.zynance.modal.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Insert a new user
    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    // Update an existing user
    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    // Delete a user
    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }

    // Get a user by ID
    fun getUserById(userId: Long): LiveData<User?> {
        val result = MutableLiveData<User?>()
        viewModelScope.launch {
            result.postValue(userRepository.getUserById(userId))
        }
        return result
    }

    // Get all users (if needed in the future)
    fun getAllUsers(): LiveData<List<User>> {
        val result = MutableLiveData<List<User>>()
        viewModelScope.launch {
            result.postValue(userRepository.getAllUsers())
        }
        return result
    }
}
