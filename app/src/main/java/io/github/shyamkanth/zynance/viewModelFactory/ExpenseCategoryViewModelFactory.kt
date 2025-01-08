package io.github.shyamkanth.zynance.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.shyamkanth.zynance.modal.repository.ExpenseCategoryRepository
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel

class ExpenseCategoryViewModelFactory(private val expenseCategoryRepository: ExpenseCategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseCategoryViewModel(expenseCategoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
