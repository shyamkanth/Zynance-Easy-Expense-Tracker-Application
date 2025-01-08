package io.github.shyamkanth.zynance.modal.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expense",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseCategory::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE // Delete expenses if the category is deleted
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Delete expenses if the user is deleted
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val expenseId: Long? = null,
    val userId: Long?, // Foreign key to associate expense with a user
    val categoryId: Long?, // Foreign key to associate expense with a category
    val expenseAmount: Double?, // Use a numeric type for monetary values
    val description: String?,
    val expenseDate: String? = null // Optional: Track when the expense occurred
)

