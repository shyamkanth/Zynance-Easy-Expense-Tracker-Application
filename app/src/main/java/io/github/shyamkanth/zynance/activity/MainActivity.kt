package io.github.shyamkanth.zynance.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.github.shyamkanth.zynance.R
import io.github.shyamkanth.zynance.Zynance
import io.github.shyamkanth.zynance.adapter.TabAdapter
import io.github.shyamkanth.zynance.databinding.ActivityMainBinding
import io.github.shyamkanth.zynance.fragment.CustomBottomSheetDialogFragment
import io.github.shyamkanth.zynance.modal.UpdateListener
import io.github.shyamkanth.zynance.modal.entity.User
import io.github.shyamkanth.zynance.utils.Utils
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseCategoryViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.UserViewModelFactory
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel
import io.github.shyamkanth.zynance.viewmodel.SharedViewModel
import io.github.shyamkanth.zynance.viewmodel.UserViewModel
import java.util.Locale

class MainActivity : AppCompatActivity(), UpdateListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseCategoryViewModel: ExpenseCategoryViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var toggleButton: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val userId: Long = 1L
    private var currentUser: User? = null
    private var totalBalance: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewPager: ViewPager2 = binding.layViewPager
        val tabLayout: TabLayout = binding.layTab
        viewPager.adapter = TabAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.item_expenses)
                1 -> tab.text = getString(R.string.item_transaction)
                2 -> tab.text = getString(R.string.item_stats)
            }
        }.attach()
        initValues()
        initClicks()
        updateValues()
    }

    @SuppressLint("SetTextI18n")
    private fun initValues() {
        val app = application as Zynance
        val userViewModelFactory = UserViewModelFactory(app.userRepository)
        val expenseViewModelFactory = ExpenseViewModelFactory(app.expenseRepository)
        val expenseCategoryViewModelFactory = ExpenseCategoryViewModelFactory(app.expenseCategoryRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]
        expenseCategoryViewModel = ViewModelProvider(this, expenseCategoryViewModelFactory)[ExpenseCategoryViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        toggleButton = binding.icMenu
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        toggleButton.setOnClickListener {
            drawerLayout.open()
        }
        updateValues()
    }

    private fun initClicks() {
        binding.layAccountCard.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
        binding.icInfo.setOnClickListener {
            val message = "You could have ${getString(R.string.rupee_amount)}${totalBalance ?: "0.0"} in your bank, if you had stopped wasting money from ${currentUser?.creationDate?.take(10)}."
            CustomBottomSheetDialogFragment.showDialog(this, Utils.BsType.INFO, "Balance Fetched Successfully", message.capitalize(Locale.ROOT))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateValues(){
        userViewModel.getUserById(userId).observe(this){ user ->
            if(user == null){
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                currentUser = user
                user.let { it ->
                    binding.tvBankName.text = it.bankName?.uppercase()
                    binding.tvAccountNumber.text = it.accountNumber?.takeLast(4)
                    binding.tvValidFrom.text = it.accountOpeningDate?.take(10)
                    binding.tvUserName.text = "Hello, ${it.name}"
                    navView.getHeaderView(0).findViewById<TextView>(R.id.tvUserName).text = "${it.name}"
                    navView.getHeaderView(0).findViewById<TextView>(R.id.tvBankName).text = "${it.bankName?.uppercase()}"
                    navView.getHeaderView(0).findViewById<TextView>(R.id.tvAccountNumber).text = "Account Number: ${it.accountNumber}"
                    navView.setNavigationItemSelectedListener { menuItem ->
                        when(menuItem.itemId) {
                            R.id.home -> {
                                drawerLayout.close()
                                true
                            }
                            R.id.close -> {
                                drawerLayout.close()
                                true
                            }
                            R.id.account -> {
                                drawerLayout.close()
                                val intent = Intent(this, InfoActivity::class.java)
                                startActivity(intent)
                                true
                            }
                            R.id.manageCategory -> {
                                drawerLayout.close()
                                expenseCategoryViewModel.getCategoriesByUserId(1L){ list ->
                                    Utils.showManageCategoryDialog(this, list)
                                }
                                true
                            }
                            R.id.addCategory -> {
                                drawerLayout.close()
                                Utils.showAddExpenseCategoryDialog(this){ category ->
                                    expenseCategoryViewModel.insertCategory(category)
                                    Utils.showAlertDialogInSameActivity(this, Utils.BsType.SUCCESS, "Category Added Successfully")
                                }
                                true
                            }
                            R.id.addExpense -> {
                                drawerLayout.close()
                                expenseCategoryViewModel.getCategoriesByUserId(1L) { list ->
                                    Utils.showAddExpenseDialog(this, list) { expense ->
                                        expenseViewModel.insertExpense(expense)
                                        updateValues()
                                        sharedViewModel.triggerRefresh()
                                        Utils.showAlertDialogInSameActivity(this, Utils.BsType.SUCCESS, "Expense Added Successfully")
                                    }
                                }
                                true
                            }

                            else -> {false}
                        }
                    }
                }
            }
        }
        expenseViewModel.getTotalExpenseByUser(1L){ total ->
            totalBalance = total
            if(total == null){
                binding.tvBankBalance.text = getString(R.string.rupee_amount) + "0.0"
            } else {
                binding.tvBankBalance.text = getString(R.string.rupee_amount) + total.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateValues()
    }

    override fun shouldUpdate(flag: Boolean) {
        if(flag){
            updateValues()
        }
    }
}