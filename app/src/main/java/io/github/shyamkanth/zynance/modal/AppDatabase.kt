package io.github.shyamkanth.zynance.modal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.shyamkanth.zynance.modal.dao.ExpenseCategoryDao
import io.github.shyamkanth.zynance.modal.dao.ExpenseDao
import io.github.shyamkanth.zynance.modal.dao.UserDao
import io.github.shyamkanth.zynance.modal.entity.Expense
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import io.github.shyamkanth.zynance.modal.entity.User

@Database(entities = [User::class, ExpenseCategory::class, Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // DAOs for the entities
    abstract fun userDao(): UserDao
    abstract fun expenseCategoryDao(): ExpenseCategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Handles schema changes gracefully during development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
