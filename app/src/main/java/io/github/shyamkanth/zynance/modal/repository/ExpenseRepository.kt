package io.github.shyamkanth.zynance.modal.repository

import io.github.shyamkanth.zynance.modal.dao.ExpenseDao
import io.github.shyamkanth.zynance.modal.entity.Expense

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    // Insert a new expense
    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    // Update an existing expense
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    // Delete an expense
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    // Get all expenses for a specific category
    suspend fun getExpensesByCategoryId(categoryId: Long): List<Expense> {
        return expenseDao.getExpensesByCategoryId(categoryId)
    }

    // Get all expenses for a specific user
    suspend fun getExpensesByUserId(userId: Long): List<Expense> {
        return expenseDao.getExpensesByUserId(userId)
    }

    // Get a single expense by its ID
    suspend fun getExpenseById(expenseId: Long): Expense? {
        return expenseDao.getExpenseById(expenseId)
    }

    // Get total expense amount for a specific category
    suspend fun getTotalExpenseByCategory(categoryId: Long): Double? {
        return expenseDao.getTotalExpenseByCategory(categoryId)
    }

    // Get total expense amount for a specific user
    suspend fun getTotalExpenseByUser(userId: Long): Double? {
        return expenseDao.getTotalExpenseByUser(userId)
    }
}
