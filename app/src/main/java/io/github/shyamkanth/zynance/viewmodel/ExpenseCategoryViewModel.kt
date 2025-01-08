package io.github.shyamkanth.zynance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import io.github.shyamkanth.zynance.modal.repository.ExpenseCategoryRepository
import kotlinx.coroutines.launch

class ExpenseCategoryViewModel(private val expenseCategoryRepository: ExpenseCategoryRepository) : ViewModel() {

    // Insert a new category
    fun insertCategory(expenseCategory: ExpenseCategory) {
        viewModelScope.launch {
            expenseCategoryRepository.insertCategory(expenseCategory)
        }
    }

    // Update an existing category
    fun updateCategory(expenseCategory: ExpenseCategory) {
        viewModelScope.launch {
            expenseCategoryRepository.updateCategory(expenseCategory)
        }
    }

    // Delete a category
    fun deleteCategory(expenseCategory: ExpenseCategory) {
        viewModelScope.launch {
            expenseCategoryRepository.deleteCategory(expenseCategory)
        }
    }

    // Get all categories for a specific user
    fun getCategoriesByUserId(userId: Long, onResult: (List<ExpenseCategory>) -> Unit) {
        viewModelScope.launch {
            val categories = expenseCategoryRepository.getCategoriesByUserId(userId)
            onResult(categories)
        }
    }

    // Get a single category by its ID
    fun getCategoryById(categoryId: Long, onResult: (ExpenseCategory?) -> Unit) {
        viewModelScope.launch {
            val category = expenseCategoryRepository.getCategoryById(categoryId)
            onResult(category)
        }
    }
}
