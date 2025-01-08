package io.github.shyamkanth.zynance.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.shyamkanth.zynance.modal.repository.ExpenseRepository
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel

class ExpenseViewModelFactory(private val expenseRepository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(expenseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
