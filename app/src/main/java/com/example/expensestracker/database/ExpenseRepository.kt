package com.example.expensestracker.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseRepository(val expenseDao: ExpenseDao) {

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()
    val allTags: LiveData<List<String>> = expenseDao.getAllTags()

    fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }

    fun update(expense: Expense) {
        expenseDao.update(expense)
    }

    fun getAllExpensesFiltered(tag : String) : LiveData<List<Expense>> {
        return expenseDao.getAllExpensesFiltered(tag)
    }

}