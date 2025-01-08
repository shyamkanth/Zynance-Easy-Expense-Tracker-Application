package io.github.shyamkanth.zynance.modal.repository

import io.github.shyamkanth.zynance.modal.dao.UserDao
import io.github.shyamkanth.zynance.modal.entity.User

class UserRepository(private val userDao: UserDao) {

    // Insert a new user
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    // Update an existing user
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    // Delete a user
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    // Get a user by their ID
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    // Get all users (if multi-user support is needed in the future)
    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }
}
