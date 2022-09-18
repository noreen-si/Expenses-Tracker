package com.example.expensestracker.expenses

import android.app.Application
import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.example.expensestracker.database.Expense
import com.example.expensestracker.database.ExpenseDatabase
import com.example.expensestracker.database.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class ExpensesViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>
    val allTags: LiveData<List<String>>
    val tagFilter : MutableLiveData<String>

    init {
        val dao = ExpenseDatabase.getInstance(application).expenseDao
        repository = ExpenseRepository(dao)
        tagFilter = MutableLiveData<String>()
        allExpenses = Transformations.switchMap(tagFilter) {
            tagString -> repository.getAllExpensesFiltered(tagString)
        }
        allTags = repository.allTags
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
         repository.update(expense)
    }

    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(expense)
    }

    fun deleteExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(expense)
    }
}