package io.github.shyamkanth.zynance.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import io.github.shyamkanth.zynance.databinding.CategoryListItemBinding
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import io.github.shyamkanth.zynance.utils.Utils
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel
import io.github.shyamkanth.zynance.viewmodel.SharedViewModel

class CategoryAdapter(
    private val context: Context,
    private val activity: Activity,
    private val categoryList: MutableList<ExpenseCategory>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private lateinit var expenseCategoryViewModel: ExpenseCategoryViewModel
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val binding = CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        expenseCategoryViewModel = ViewModelProvider(activity as ViewModelStoreOwner)[ExpenseCategoryViewModel::class.java]
        sharedViewModel = ViewModelProvider(activity)[SharedViewModel::class.java]
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.tvCategoryName.text = category.categoryName
        holder.binding.icEdit.setOnClickListener {
            Utils.showEditExpenseCategoryDialog(activity, category) { newCategory ->
                expenseCategoryViewModel.updateCategory(newCategory)
                categoryList[position] = newCategory
                notifyItemChanged(position)
                sharedViewModel.triggerRefresh()
                Utils.showAlertDialogInSameActivity(activity, Utils.BsType.SUCCESS, "Category Updated Successfully")
            }
        }
        holder.binding.icDelete.setOnClickListener {
            Utils.showDeleteDialog(activity){
                expenseCategoryViewModel.deleteCategory(category)
                categoryList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, categoryList.size)
                sharedViewModel.triggerRefresh()
                Utils.showAlertDialogInSameActivity(activity, Utils.BsType.SUCCESS, "Category Deleted Successfully")
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root)


}