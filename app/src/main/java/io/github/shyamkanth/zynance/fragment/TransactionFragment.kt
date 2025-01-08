package io.github.shyamkanth.zynance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.shyamkanth.zynance.Zynance
import io.github.shyamkanth.zynance.adapter.TransactionAdapter
import io.github.shyamkanth.zynance.databinding.FragmentTransactionBinding
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseCategoryViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseViewModelFactory
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel
import io.github.shyamkanth.zynance.viewmodel.SharedViewModel

class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var expenseCategoryViewModel: ExpenseCategoryViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)
        initValues()
        sharedViewModel.refreshTrigger.observe(viewLifecycleOwner){
            updateTransaction()
        }
        updateTransaction()
        return binding.root
    }

    private fun initValues() {
        val app = requireActivity().application as Zynance
        val expenseViewModelFactory = ExpenseViewModelFactory(app.expenseRepository)
        val expenseCategoryViewModelFactory = ExpenseCategoryViewModelFactory(app.expenseCategoryRepository)
        expenseCategoryViewModel = ViewModelProvider(this, expenseCategoryViewModelFactory)[ExpenseCategoryViewModel::class.java]
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.refreshTrigger.observe(viewLifecycleOwner){
            updateTransaction()
        }
        updateTransaction()
    }

    private fun updateTransaction() {
        expenseCategoryViewModel.getCategoriesByUserId(1L){ expenseCategories ->
            expenseViewModel.getExpensesByUserId(1L){ expenseList ->
                val rvList = binding.rvTransactionList
                val adapter = TransactionAdapter(requireContext(), requireActivity(), expenseList, expenseCategories)
                rvList.adapter = adapter
                val layoutManager = LinearLayoutManager(this.requireContext())
                layoutManager.stackFromEnd = true
                layoutManager.reverseLayout = true
                rvList.layoutManager = layoutManager
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateTransaction()
    }

}