package com.example.expensestracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses_table")
data class Expense(
    @ColumnInfo(name = "total_cost")
    val totalCost: Double = 0.00,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: String,

    @ColumnInfo(name = "tag")
    val tag: String
) {
    @PrimaryKey(autoGenerate = true)
    var expenseId: Long = 0L
}