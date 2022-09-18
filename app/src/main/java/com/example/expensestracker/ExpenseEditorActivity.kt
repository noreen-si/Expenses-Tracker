package com.example.expensestracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.expensestracker.database.Expense
import com.example.expensestracker.databinding.ActivityExpenseEditorBinding
import com.example.expensestracker.expenses.ExpensesViewModel
import java.text.SimpleDateFormat
import java.util.*

class ExpenseEditorActivity : AppCompatActivity() {
    lateinit var binding: ActivityExpenseEditorBinding
    lateinit var expenseEditTitle : EditText
    lateinit var expenseEditDesc : EditText
    lateinit var editCost : EditText
    lateinit var updateEditButton : Button
    lateinit var editTag : EditText
    lateinit var viewModel : ExpensesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up binding, set up our views accordingly
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_editor)
        expenseEditTitle = binding.editExpenseTitle
        expenseEditDesc = binding.editDesc
        editCost = binding.editCost
        editTag = binding.editTag
        updateEditButton = binding.updateButton
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application))[ExpensesViewModel::class.java]

        val expenseType = intent.getStringExtra("expenseType")
        val expenseID : Long = intent.getLongExtra("expenseID", -1)

        // Editing a pre-existing expense
        if (expenseType.equals("Edit")) {
            val expenseTitle = intent.getStringExtra("expenseTitle")
            val expenseDesc = intent.getStringExtra("expenseDesc")
            val expenseCost: Double = intent.getDoubleExtra("expenseCost", 0.00)
            val expenseTag: String = intent.getStringExtra("expenseTag")!!

            expenseEditTitle.setText(expenseTitle)
            expenseEditDesc.setText(expenseDesc)
            editCost.setText(expenseCost.toString())
            editTag.setText(expenseTag)
        } else {
            // Add a new expense
            updateEditButton.setText("Save")
        }

        // Listener for our update/edit button
        updateEditButton.setOnClickListener {
            // When button is pressed, make sure to update our expense in database
            val title = expenseEditTitle.text.toString()
            val desc = expenseEditDesc.text.toString()
            val cost : Double = editCost.text.toString().toDouble()
            val date : String = SimpleDateFormat("MMM dd, yyyy - HH:mm").format(Date())
            val tag : String = editTag.text.toString()

            if(expenseType.equals("Edit") && title.isNotEmpty() && desc.isNotEmpty() && cost != 0.00) {
                // Making changes to pre-existing expense
                val updater = Expense(cost, title, desc, date, tag)
                updater.expenseId = expenseID
                viewModel.updateExpense(updater)
            } else {
                // Adding new expense
                if (title.isNotEmpty() && cost != 0.00) {
                    viewModel.addExpense(Expense(cost, title, desc, date, tag))
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}