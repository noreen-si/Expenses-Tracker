package com.example.expensestracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensestracker.database.Expense
import com.example.expensestracker.databinding.ActivityMainBinding
import com.example.expensestracker.expenses.ExpensesAdapter
import com.example.expensestracker.expenses.ExpensesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity(), ExpensesAdapter.ClickInterface, ExpensesAdapter.DeleteInterface, AdapterView.OnItemSelectedListener {
    lateinit var expenseRV: RecyclerView
    lateinit var addExpenseFAB: FloatingActionButton
    lateinit var spinner : Spinner
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ExpensesViewModel
    lateinit var expenseRVAdapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        expenseRV = binding.RVExpenses
        addExpenseFAB = binding.FabExpense
        spinner = binding.spinner
        expenseRV.layoutManager = LinearLayoutManager(this)

        // Set up RV adapter
        expenseRVAdapter = ExpensesAdapter(this, this, this)
        expenseRV.adapter = expenseRVAdapter
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ExpensesViewModel::class.java]

        // Observe changes in allExpenses list
        viewModel.allExpenses.observe(this) { list ->
            list?.let {
                expenseRVAdapter.updateList(it)
            }
        }

        // Set listener for floating action button
        addExpenseFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, ExpenseEditorActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        // Set up our spinnerAdapter
        val spinnerAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrayList())
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = spinnerAdapter

        // Set observer for allTags to update what shows up on spinner
        viewModel.allTags.observe(this) { list ->
            list?.let {
                spinnerAdapter.clear()
                spinnerAdapter.addAll(it)
                spinnerAdapter.notifyDataSetChanged()
            }
        }

        // Most importantly, set listener for spinner
        spinner.onItemSelectedListener = this

        // Observe tag filter to appropriately update recycler view
        viewModel.tagFilter.observe(this) {
            Observer<List<Expense>> { t ->
                if (t != null) {
                    expenseRVAdapter.updateList(t)
                }
            }
        }

    }

    override fun onDelete(expense: Expense) {
        viewModel.deleteExpense(expense)
    }

    // Launch editor activity with all necessary information
    override fun onClick(expense: Expense) {
        val intent = Intent(this@MainActivity, ExpenseEditorActivity::class.java)
        intent.putExtra("expenseType", "Edit")
        intent.putExtra("expenseTitle", expense.title)
        intent.putExtra("expenseID", expense.expenseId)
        intent.putExtra("expenseDesc", expense.description)
        intent.putExtra("expenseCost", expense.totalCost)
        intent.putExtra("expenseTag", expense.tag)
        startActivity(intent)
    }

    // When an item is selected, change the tag filter accordingly
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val tag : String = spinner.getItemAtPosition(position).toString()
        viewModel.tagFilter.value = tag

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}