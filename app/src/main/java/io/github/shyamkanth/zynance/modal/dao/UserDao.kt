package io.github.shyamkanth.zynance.modal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.shyamkanth.zynance.modal.entity.User

@Dao
interface UserDao {

    // Insert a user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Update user details
    @Update
    suspend fun updateUser(user: User)

    // Delete a user
    @Delete
    suspend fun deleteUser(user: User)

    // Get the single user (assuming one user for the app)
    @Query("SELECT * FROM user_info WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    // Optional: Get all users (if multi-user support is needed in the future)
    @Query("SELECT * FROM user_info")
    suspend fun getAllUsers(): List<User>
}
