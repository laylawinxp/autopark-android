package com.example.autopark.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autopark.dto.UsersDto
import com.example.autopark.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {
    private val repository = UsersRepository()

    private val _users = MutableStateFlow<List<UsersDto>>(emptyList())
    val users: StateFlow<List<UsersDto>> get() = _users

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun getUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedUsers = repository.getUsers()
                _users.value = fetchedUsers
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error fetching users: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addUser(userDto: UsersDto) {
        viewModelScope.launch {
            try {
                Log.d("UsersViewModel", "Adding user: $userDto")

                val addedUser = repository.addUser(userDto)

                _users.value = _users.value.toMutableList().apply {
                    add(addedUser)
                }

                Log.d("UsersViewModel", "User added successfully: $addedUser")
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error adding user: ${e.message}", e)
                getUsers()
            }
        }
    }

    fun updateUser(id: Int, userDto: UsersDto) {
        viewModelScope.launch {
            try {
                repository.updateUser(id, userDto)
                _users.value = _users.value.map {
                    if (it.id == id) userDto else it
                }
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error updating user: ${e.message}", e)
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                _users.value = _users.value.filterNot { it.id == id }
                repository.deleteUser(id)
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error deleting user: ${e.message}", e)
            }
        }
    }
}
