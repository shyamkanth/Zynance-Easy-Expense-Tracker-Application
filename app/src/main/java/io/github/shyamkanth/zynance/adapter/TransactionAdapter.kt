package io.github.shyamkanth.zynance.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import io.github.shyamkanth.zynance.databinding.TransactionListItemBinding
import io.github.shyamkanth.zynance.fragment.CustomBottomSheetDialogFragment
import io.github.shyamkanth.zynance.modal.entity.Expense
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import io.github.shyamkanth.zynance.utils.Utils

class TransactionAdapter(
    private val context: Context,
    private val activity: Activity,
    private val expenses: List<Expense>,
    private val expenseCategories: List<ExpenseCategory>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.ViewHolder {
        val binding = TransactionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.binding.expenseAmount.text = expense.expenseAmount.toString()
        val category = expenseCategories.find {
            it.categoryId == expense.categoryId
        }
        holder.binding.categoryName.text = category?.categoryName
        holder.binding.expenseDate.text = "Date: "+expense.expenseDate?.take(10)
        holder.binding.expenseTime.text = "Time: "+expense.expenseDate?.takeLast(8)
        val imageBitmap = Utils.generateImageInitials(context, category?.categoryName?.uppercase() ?: "", position)
        holder.binding.imgPerson.setImageBitmap(imageBitmap)
        holder.itemView.setOnClickListener {
            val message = "You have spent ${expense.expenseAmount} on ${category?.categoryName} on ${expense.expenseDate?.take(10)} at ${expense.expenseDate?.takeLast(8)}."
            CustomBottomSheetDialogFragment.showDialog(activity as FragmentActivity, Utils.BsType.INFO, "Expense Details", message)
        }
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    inner class ViewHolder(val binding: TransactionListItemBinding) : RecyclerView.ViewHolder(binding.root)
}