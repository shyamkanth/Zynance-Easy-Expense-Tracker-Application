package io.github.shyamkanth.zynance.modal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class User(
    @PrimaryKey(autoGenerate = false) val id: Long = 1,
    val name: String?,
    val bankName: String?,
    val accountOpeningDate: String?,
    val accountNumber: String?,
    val bankCode: String?,
    val currentBalance: Double,
    val creationDate: String? = null
)
