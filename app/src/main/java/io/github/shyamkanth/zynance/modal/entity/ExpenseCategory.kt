package io.github.shyamkanth.zynance.modal.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expense_category",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Delete categories if the user is deleted
        )
    ]
)
data class ExpenseCategory(
    @PrimaryKey(autoGenerate = true) val categoryId: Long,
    val userId: Long?,
    val categoryName: String?,
    val categoryDescription: String? = null
)
