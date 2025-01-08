package io.github.shyamkanth.zynance.modal.repository

import io.github.shyamkanth.zynance.modal.dao.ExpenseCategoryDao
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory

class ExpenseCategoryRepository(private val expenseCategoryDao: ExpenseCategoryDao) {

    suspend fun insertCategory(expenseCategory: ExpenseCategory) {
        expenseCategoryDao.insertCategory(expenseCategory)
    }

    suspend fun updateCategory(expenseCategory: ExpenseCategory) {
        expenseCategoryDao.updateCategory(expenseCategory)
    }

    suspend fun deleteCategory(expenseCategory: ExpenseCategory) {
        expenseCategoryDao.deleteCategory(expenseCategory)
    }

    suspend fun getCategoriesByUserId(userId: Long): List<ExpenseCategory> {
        return expenseCategoryDao.getCategoriesByUserId(userId)
    }

    suspend fun getCategoryById(categoryId: Long): ExpenseCategory? {
        return expenseCategoryDao.getCategoryById(categoryId)
    }
}
