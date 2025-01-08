package io.github.shyamkanth.zynance.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import io.github.shyamkanth.zynance.Zynance
import io.github.shyamkanth.zynance.databinding.FragmentStatsBinding
import io.github.shyamkanth.zynance.modal.entity.Expense
import io.github.shyamkanth.zynance.utils.Utils
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseCategoryViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseViewModelFactory
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel
import io.github.shyamkanth.zynance.viewmodel.SharedViewModel

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseCategoryViewModel: ExpenseCategoryViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        pieChart = binding.pieChart
        barChart = binding.barChart
        initValues()
        sharedViewModel.refreshTrigger.observe(viewLifecycleOwner){
            if(it){
                super.onResume()
            }
        }
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
            if(it){
                super.onResume()
            }
        }
        binding.filterRadioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                binding.radioBar.id -> {
                    binding.pieChart.visibility = View.GONE
                    binding.barChart.visibility = View.VISIBLE
                    binding.tableLayout.visibility = View.GONE
                    binding.layTableScroll.visibility = View.GONE
                    setupBarChart()
                    loadBarChartData()
                }
                binding.radioPie.id -> {
                    binding.pieChart.visibility = View.VISIBLE
                    binding.barChart.visibility = View.GONE
                    binding.tableLayout.visibility = View.GONE
                    binding.layTableScroll.visibility = View.GONE
                    setupPieChart()
                    loadPieChartData()
                }
                binding.radioTable.id -> {
                    binding.pieChart.visibility = View.GONE
                    binding.barChart.visibility = View.GONE
                    binding.tableLayout.visibility = View.VISIBLE
                    binding.layTableScroll.visibility = View.VISIBLE
                    loadTableData()
                }
            }
        }
        binding.filterRadioGroup.check(binding.radioPie.id)
    }

    private fun loadTableData() {
        val tableLayout = binding.tableLayout
        tableLayout.removeViewsInLayout(1, tableLayout.childCount-1)
        var serialNumber = 1
        expenseViewModel.getExpensesByUserId(1L) { expenseList ->
            expenseCategoryViewModel.getCategoriesByUserId(1L) { categoryList ->
                for (category in categoryList) {
                    val totalExpense = expenseList.filter { it.categoryId == category.categoryId }
                        .sumOf { it.expenseAmount ?: 0.0 }
                    val row = TableRow(requireContext())
                    if(totalExpense > 0){
                        val serialNumberTextView = TextView(requireContext())
                        val categoryNameTextView = TextView(requireContext())
                        val totalExpenseTextView = TextView(requireContext())
                        serialNumberTextView.text = serialNumber.toString()
                        categoryNameTextView.text = category.categoryName
                        totalExpenseTextView.text = totalExpense.toString()
                        serialNumberTextView.setPadding(16, 16, 16, 16)
                        categoryNameTextView.setPadding(16, 16, 16, 16)
                        totalExpenseTextView.setPadding(16, 16, 16, 16)
                        categoryNameTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        totalExpenseTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        row.addView(serialNumberTextView)
                        row.addView(categoryNameTextView)
                        row.addView(totalExpenseTextView)
                        tableLayout.addView(row)
                        serialNumber++
                    }
                }
            }
        }
    }

    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.holeRadius = 58f
        pieChart.setTransparentCircleAlpha(0)

        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Expenses"
        pieChart.setCenterTextSize(22f)

        pieChart.description.isEnabled = true
        pieChart.description.text = "Total Expenses By Categories"
        pieChart.legend.isEnabled = true
        pieChart.legend.isWordWrapEnabled = true
        pieChart.legend.form = Legend.LegendForm.CIRCLE
    }

    private fun loadPieChartData() {
        expenseViewModel.getExpensesByUserId(1L) { expenseList ->
            expenseCategoryViewModel.getCategoriesByUserId(1L) { categoryList ->
                val pieEntries = mutableListOf<PieEntry>()
                for (category in categoryList) {
                    val totalExpense = expenseList.filter { it.categoryId == category.categoryId }.sumOf { it.expenseAmount ?: 0.0}
                    if(totalExpense > 0){
                        pieEntries.add(PieEntry(totalExpense.toFloat(), category.categoryName))
                    }
                }

                val dataSet = PieDataSet(pieEntries, "")
                dataSet.colors = Utils.colors
                dataSet.sliceSpace = 2f
                dataSet.valueTextColor = Color.WHITE
                dataSet.valueTextSize = 12f

                val data = PieData(dataSet)
                pieChart.data = data
                pieChart.invalidate()
                pieChart.animateY(1400)
            }
        }
    }

    private fun setupBarChart() {
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setMaxVisibleValueCount(60)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setGranularity(1f)
        xAxis.isGranularityEnabled = true

        val leftAxis = barChart.axisLeft
        leftAxis.isEnabled = true
        barChart.legend.isEnabled = true
        barChart.legend.isWordWrapEnabled = true
        barChart.axisRight.isEnabled = false

        barChart.description.isEnabled = false
    }

    private fun loadBarChartData() {
        expenseViewModel.getExpensesByUserId(1L) { expenseList ->
            expenseCategoryViewModel.getCategoriesByUserId(1L) { categoryList ->
                try {
                    val barEntries = mutableListOf<BarEntry>()
                    var index = 0
                    for (category in categoryList) {
                        val totalExpense = expenseList.filter { it.categoryId == category.categoryId }.sumOf { it.expenseAmount ?: 0.0 }

                        if(totalExpense > 0){
                            barEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
                            index++
                        }
                    }

                    val dataSet = BarDataSet(barEntries, "Expense Categories")
                    dataSet.colors = Utils.colors
                    dataSet.valueTextColor = Color.BLUE
                    dataSet.valueTextSize = 12f

                    val data = BarData(dataSet)
                    barChart.data = data
                    barChart.invalidate()
                    barChart.animateY(1400)
                } catch (e : Exception){
                    Log.d("test", e.message.toString())
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        barChart.clear()
        if(binding.filterRadioGroup.checkedRadioButtonId == binding.radioBar.id){
            binding.pieChart.visibility = View.GONE
            binding.barChart.visibility = View.VISIBLE
            binding.tableLayout.visibility = View.GONE
            binding.layTableScroll.visibility = View.GONE
            setupBarChart()
            loadBarChartData()
        } else if(binding.filterRadioGroup.checkedRadioButtonId == binding.radioPie.id){
            binding.pieChart.visibility = View.VISIBLE
            binding.barChart.visibility = View.GONE
            binding.tableLayout.visibility = View.GONE
            binding.layTableScroll.visibility = View.GONE
            setupPieChart()
            loadPieChartData()
        } else {
            binding.pieChart.visibility = View.GONE
            binding.barChart.visibility = View.GONE
            binding.tableLayout.visibility = View.VISIBLE
            binding.layTableScroll.visibility = View.VISIBLE
            loadTableData()
        }
    }
}
