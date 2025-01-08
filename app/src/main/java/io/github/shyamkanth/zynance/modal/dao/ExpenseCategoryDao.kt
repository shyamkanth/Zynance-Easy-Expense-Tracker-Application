package io.github.shyamkanth.zynance.modal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory

@Dao
interface ExpenseCategoryDao {

    // Insert a new category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: ExpenseCategory)

    // Update an existing category
    @Update
    suspend fun updateCategory(category: ExpenseCategory)

    // Delete a category
    @Delete
    suspend fun deleteCategory(category: ExpenseCategory)

    // Get all categories for a specific user
    @Query("SELECT * FROM expense_category WHERE userId = :userId")
    suspend fun getCategoriesByUserId(userId: Long): List<ExpenseCategory>

    // Optional: Get a single category by its ID
    @Query("SELECT * FROM expense_category WHERE categoryId = :categoryId")
    suspend fun getCategoryById(categoryId: Long): ExpenseCategory?
}
