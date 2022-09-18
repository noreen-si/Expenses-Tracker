package com.example.expensestracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {
    @Insert
    fun insert(expense: Expense)

    @Update
    fun update(expense: Expense)

    @Delete
    fun delete(expense: Expense)

    @Query("SELECT * FROM expenses_table ORDER BY expenseId ASC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT DISTINCT tag FROM expenses_table")
    fun getAllTags(): LiveData<List<String>>

    @Query("SELECT * FROM expenses_table WHERE tag = :tag")
    fun getAllExpensesFiltered(tag : String): LiveData<List<Expense>>


}