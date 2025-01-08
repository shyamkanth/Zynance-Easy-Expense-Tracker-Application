package io.github.shyamkanth.zynance.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import io.github.shyamkanth.zynance.databinding.ExpenseListItemBinding
import io.github.shyamkanth.zynance.modal.UpdateListener
import io.github.shyamkanth.zynance.modal.entity.Expense
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import io.github.shyamkanth.zynance.utils.Utils
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel

class ExpenseAdapter(
    private val context: Context,
    private val activity: Activity,
    private val expenseList: List<Expense>,
    private val expenseCategories: List<ExpenseCategory>,
    private val expenseViewModel: ExpenseViewModel,
    private val shouldUpdate: MutableLiveData<Boolean>
) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ExpenseAdapter.ViewHolder {
        val binding =
            ExpenseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExpenseAdapter.ViewHolder, position: Int) {
        holder.binding.expenseAmount.text = expenseList[position].expenseAmount.toString()
        holder.binding.expenseDate.text = "Date & Time : "+expenseList[position].expenseDate
        val category = expenseCategories.find {
            it.categoryId == expenseList[position].categoryId
        }
        holder.binding.categoryName.text = category?.categoryName.toString()
        holder.itemView.setOnClickListener {
            Utils.showAddExpenseDialogWithDetail(activity, expenseList[position], expenseCategories) { updatedExpense, flag ->
                if(flag){
                    Utils.showDeleteDialog(activity){
                        expenseViewModel.deleteExpense(updatedExpense)
                        shouldUpdate.postValue(true)
                        Utils.showAlertDialogInSameActivity(activity, Utils.BsType.SUCCESS, "Expense Deleted Successfully")
                    }
                } else {
                    expenseViewModel.updateExpense(updatedExpense)
                    shouldUpdate.postValue(true)
                    Utils.showAlertDialogInSameActivity(activity, Utils.BsType.SUCCESS, "Expense Updated Successfully")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    inner class ViewHolder(val binding: ExpenseListItemBinding) : RecyclerView.ViewHolder(binding.root)
}