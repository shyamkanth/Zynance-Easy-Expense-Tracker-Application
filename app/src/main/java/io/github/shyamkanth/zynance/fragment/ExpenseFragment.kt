package io.github.shyamkanth.zynance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.shyamkanth.zynance.Zynance
import io.github.shyamkanth.zynance.adapter.ExpenseAdapter
import io.github.shyamkanth.zynance.databinding.FragmentExpenseBinding
import io.github.shyamkanth.zynance.modal.UpdateListener
import io.github.shyamkanth.zynance.utils.Utils
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseCategoryViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseViewModelFactory
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel
import io.github.shyamkanth.zynance.viewmodel.SharedViewModel

class ExpenseFragment : Fragment(), UpdateListener{
    private lateinit var binding: FragmentExpenseBinding
    private lateinit var expenseCategoryViewModel: ExpenseCategoryViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var updateListener: UpdateListener? = null
    private val shouldUpdate: MutableLiveData<Boolean> = MutableLiveData(false)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseBinding.inflate(layoutInflater, container, false)
        initValues()
        setupClicks()
        shouldUpdate.observe(viewLifecycleOwner) {
            if (it) {
                updateList()
            }
        }
        return binding.root
    }

    private fun initValues(){
        val app = requireActivity().application as Zynance
        val expenseCategoryViewModelFactory = ExpenseCategoryViewModelFactory(app.expenseCategoryRepository)
        val expenseViewModelFactory = ExpenseViewModelFactory(app.expenseRepository)
        expenseCategoryViewModel = ViewModelProvider(this, expenseCategoryViewModelFactory)[ExpenseCategoryViewModel::class.java]
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.refreshTrigger.observe(viewLifecycleOwner) {
            if (it) {
                updateList()
            }
        }
        updateList()
    }

    private fun setupClicks(){
        binding.icAddExpense.setOnClickListener {
            Utils.showPopupAboveButton(requireActivity(), it) { result ->
                when(result) {
                    Utils.AddType.EXPENSE -> {
                        expenseCategoryViewModel.getCategoriesByUserId(1L) { list ->
                            Utils.showAddExpenseDialog(requireActivity(), list) { expense ->
                                expenseViewModel.insertExpense(expense)
                                sharedViewModel.triggerRefresh()
                                updateList()
                                Utils.showAlertDialogInSameActivity(requireActivity(), Utils.BsType.SUCCESS, "Expense Added Successfully")
                            }
                        }
                    }
                    Utils.AddType.CATEGORY -> {
                        Utils.showAddExpenseCategoryDialog(requireActivity()) { category ->
                            expenseCategoryViewModel.insertCategory(category)
                            Utils.showAlertDialogInSameActivity(requireActivity(), Utils.BsType.SUCCESS, "Category Added Successfully")
                        }
                    }
                }
            }
        }

    }

    private fun updateList() {
        updateListener?.shouldUpdate(true)
        expenseCategoryViewModel.getCategoriesByUserId(1L){ expenseCategories ->
            expenseViewModel.getExpensesByUserId(1L){ expenseList ->
                val rvList = binding.rvExpenseList
                val adapter = ExpenseAdapter(this.requireContext(), requireActivity(), expenseList, expenseCategories, expenseViewModel, shouldUpdate)
                rvList.adapter = adapter
                val layoutManager = LinearLayoutManager(this.requireContext())
                layoutManager.stackFromEnd = true
                layoutManager.reverseLayout = true
                rvList.layoutManager = layoutManager
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UpdateListener) {
            updateListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        updateListener = null
    }

    override fun shouldUpdate(flag: Boolean) {
        if(flag){
            updateList()
        }
    }
}