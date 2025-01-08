package io.github.shyamkanth.zynance.modal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.shyamkanth.zynance.modal.entity.Expense

@Dao
interface ExpenseDao {

    // Insert a new expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    // Update an existing expense
    @Update
    suspend fun updateExpense(expense: Expense)

    // Delete an expense
    @Delete
    suspend fun deleteExpense(expense: Expense)

    // Get all expenses for a specific category
    @Query("SELECT * FROM expense WHERE categoryId = :categoryId")
    suspend fun getExpensesByCategoryId(categoryId: Long): List<Expense>

    // Get all expenses for a specific user
    @Query("SELECT * FROM expense WHERE userId = :userId")
    suspend fun getExpensesByUserId(userId: Long): List<Expense>

    // Optional: Get a single expense by its ID
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    suspend fun getExpenseById(expenseId: Long): Expense?

    // Get total expense amount for a specific category
    @Query("SELECT SUM(expenseAmount) FROM expense WHERE categoryId = :categoryId")
    suspend fun getTotalExpenseByCategory(categoryId: Long): Double?

    // Get total expense amount for a specific user
    @Query("SELECT SUM(expenseAmount) FROM expense WHERE userId = :userId")
    suspend fun getTotalExpenseByUser(userId: Long): Double?
}
