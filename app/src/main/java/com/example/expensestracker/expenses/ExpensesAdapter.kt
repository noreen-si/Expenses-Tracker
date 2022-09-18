package com.example.expensestracker.expenses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Delete
import com.example.expensestracker.ExpenseEditorActivity
import com.example.expensestracker.R
import com.example.expensestracker.database.Expense

class ExpensesAdapter(
    val clickInterface: ClickInterface,
    val deleteInterface: DeleteInterface,
    val context: Context
) : RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    var allExpenses = ArrayList<Expense>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseTV = itemView.findViewById<TextView>(R.id.TVExpenseTitle)
        val descTV = itemView.findViewById<TextView>(R.id.TVdesc)
        val timeStampTV = itemView.findViewById<TextView>(R.id.TVtime)
        val deleteIV = itemView.findViewById<ImageView>(R.id.IVdelete)
        val costTV = itemView.findViewById<TextView>(R.id.TVcost)
        val tagTV = itemView.findViewById<TextView>(R.id.TVtag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.expense_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpensesAdapter.ViewHolder, position: Int) {
        holder.expenseTV.text = allExpenses[position].title
        holder.timeStampTV.text = allExpenses[position].timeStamp
        holder.descTV.text = allExpenses[position].description
        holder.costTV.text = context.resources.getString(R.string.cost, allExpenses[position].totalCost)
        holder.tagTV.text = allExpenses[position].tag

        holder.deleteIV.setOnClickListener {
            deleteInterface.onDelete(allExpenses[position])
        }

        holder.itemView.setOnClickListener {
            clickInterface.onClick(allExpenses[position])
        }
    }

    override fun getItemCount(): Int {
        return allExpenses.size
    }

    fun updateList(newList : List<Expense>) {
        allExpenses.clear()
        allExpenses.addAll(newList)
        notifyDataSetChanged()
    }

    interface ClickInterface {
        fun onClick(expense: Expense)
    }
    interface DeleteInterface {
        fun onDelete(expense: Expense)
    }
}