package io.github.shyamkanth.zynance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shyamkanth.zynance.modal.entity.Expense
import io.github.shyamkanth.zynance.modal.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    // Insert a new expense
    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.insertExpense(expense)
        }
    }

    // Update an existing expense
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.updateExpense(expense)
        }
    }

    // Delete an expense
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expense)
        }
    }

    // Get all expenses for a specific category
    fun getExpensesByCategoryId(categoryId: Long, onResult: (List<Expense>) -> Unit) {
        viewModelScope.launch {
            val expenses = expenseRepository.getExpensesByCategoryId(categoryId)
            onResult(expenses)
        }
    }

    // Get all expenses for a specific user
    fun getExpensesByUserId(userId: Long, onResult: (List<Expense>) -> Unit) {
        viewModelScope.launch {
            val expenses = expenseRepository.getExpensesByUserId(userId)
            onResult(expenses)
        }
    }

    // Get a single expense by its ID
    fun getExpenseById(expenseId: Long, onResult: (Expense?) -> Unit) {
        viewModelScope.launch {
            val expense = expenseRepository.getExpenseById(expenseId)
            onResult(expense)
        }
    }

    // Get total expense amount for a specific category
    fun getTotalExpenseByCategory(categoryId: Long, onResult: (Double?) -> Unit) {
        viewModelScope.launch {
            val total = expenseRepository.getTotalExpenseByCategory(categoryId)
            onResult(total)
        }
    }

    // Get total expense amount for a specific user
    fun getTotalExpenseByUser(userId: Long, onResult: (Double?) -> Unit) {
        viewModelScope.launch {
            val total = expenseRepository.getTotalExpenseByUser(userId)
            onResult(total)
        }
    }
}
