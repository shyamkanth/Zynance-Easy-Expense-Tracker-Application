package io.github.shyamkanth.zynance

import android.app.Application
import io.github.shyamkanth.zynance.modal.AppDatabase
import io.github.shyamkanth.zynance.modal.repository.ExpenseCategoryRepository
import io.github.shyamkanth.zynance.modal.repository.ExpenseRepository
import io.github.shyamkanth.zynance.modal.repository.UserRepository

class Zynance : Application(){
    lateinit var database: AppDatabase
        private set
    lateinit var userRepository: UserRepository
        private set
    lateinit var expenseCategoryRepository: ExpenseCategoryRepository
        private set
    lateinit var expenseRepository: ExpenseRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
        expenseCategoryRepository = ExpenseCategoryRepository(database.expenseCategoryDao())
        expenseRepository = ExpenseRepository(database.expenseDao())
    }
}