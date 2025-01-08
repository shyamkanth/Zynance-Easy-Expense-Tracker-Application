package io.github.shyamkanth.zynance.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.github.shyamkanth.zynance.R
import io.github.shyamkanth.zynance.Zynance
import io.github.shyamkanth.zynance.databinding.ActivityInfoBinding
import io.github.shyamkanth.zynance.fragment.CustomBottomSheetDialogFragment
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import io.github.shyamkanth.zynance.modal.entity.User
import io.github.shyamkanth.zynance.utils.Utils
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseCategoryViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.ExpenseViewModelFactory
import io.github.shyamkanth.zynance.viewModelFactory.UserViewModelFactory
import io.github.shyamkanth.zynance.viewmodel.ExpenseCategoryViewModel
import io.github.shyamkanth.zynance.viewmodel.ExpenseViewModel
import io.github.shyamkanth.zynance.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseCategoryViewModel: ExpenseCategoryViewModel
    private var totalBalance: Double? = null
    val userId: Long = 1L
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initValues()
        setupClick()
    }

    @SuppressLint("SetTextI18n")
    private fun initValues() {
        binding.etCurrentBalance.setText("0.0")
        binding.etCurrentBalance.isEnabled = false
        val app = application as Zynance
        val userViewModelFactory = UserViewModelFactory(app.userRepository)
        val expenseViewModelFactory = ExpenseViewModelFactory(app.expenseRepository)
        val expenseCategoryViewModelFactory = ExpenseCategoryViewModelFactory(app.expenseCategoryRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]
        expenseCategoryViewModel = ViewModelProvider(this, expenseCategoryViewModelFactory)[ExpenseCategoryViewModel::class.java]
        userViewModel.getUserById(userId).observe(this){ user ->
            user?.let {
                fillData(it)
            }
        }
    }

    private fun setupClick() {
        binding.etAccountOpeningDate.setOnClickListener {
            Utils.showDatePicker(binding.etAccountOpeningDate, this)
        }
        binding.ivInfoBankName.setOnClickListener {
            val header = "Bank Name"
            val content =
                "The bank name you provide is entirely up to you. It doesn't need to be an actual, existing bank. This field is just a placeholder and serves as an example or demonstration. Feel free to enter any name you like, whether it's real or completely imaginary. There’s no validation or restriction on this input, so you can get creative if you want. It’s designed to offer flexibility and ensure there’s no pressure to use authentic details. Rest assured, the information you enter is for your convenience and won't be used beyond this purpose. Have fun with it!."
            CustomBottomSheetDialogFragment.showDialog(this, Utils.BsType.INFO, header, content)
        }
        binding.ivInfoBankCode.setOnClickListener {
            val header = "Bank Code"
            val content =
                "The bank code is a unique identifier assigned to a specific bank branch. Typically, it is an 8- or 11-character alphanumeric code used for transactions like international wire transfers or other banking operations. Examples include SWIFT codes, commonly used for global banking, and IFSC codes, widely used in India for identifying branches during fund transfers.\n" +
                        "\n" +
                        "However, in this context, you don’t need to provide a real code. Feel free to enter any combination of 8 or 11 characters, whether it’s a real code or just a random one. This is simply for demonstration purposes—your input is entirely flexible!."
            CustomBottomSheetDialogFragment.showDialog(this, Utils.BsType.INFO, header, content)
        }
        binding.ivInfoAccountNumber.setOnClickListener {
            val header = "Account Number"
            val content =
                "The account number is typically an 11-digit numeric code that uniquely identifies a bank account within a branch. It is used for various banking activities, such as deposits, withdrawals, and fund transfers." + "\n"+"\nHowever, in this context, you don't need to provide an actual account number. Feel free to enter any 11-digit number of your choice, whether it corresponds to a real account or not. This field is designed for demonstration purposes only, so there’s no restriction or validation requiring authentic details. You can input any number that fits the format—it’s entirely up to you!"
            CustomBottomSheetDialogFragment.showDialog(this, Utils.BsType.INFO, header, content)
        }
        binding.ivInfoCurrentBalance.setOnClickListener {
            val header = "Current Balance (DISABLED)"
            val content =
                "The current balance represents the amount of money currently in your account. It can be any numeric value that you choose. Typically, this is the starting point for tracking expenses or managing finances.\n" +
                        "\n" +
                        "In this case, it’s 0 to begin tracking your expenses from scratch. This field is completely flexible and does not require validation against a real account balance.\n"

            CustomBottomSheetDialogFragment.showDialog(this, Utils.BsType.INFO, header, content)
        }
        binding.saveButton.setOnClickListener {
            val name = binding.etName.text?.trim()
            val bankName = binding.etBankName.text?.trim()
            val accountOpeningDate = binding.etAccountOpeningDate.text?.trim()
            val accountNumber = binding.etAccountNumber.text?.trim()
            val bankCode = binding.etBankCode.text?.trim()
            if(name.isNullOrEmpty()){
                binding.etName.error = "Name is Required"
                return@setOnClickListener
            }
            if(bankName.isNullOrEmpty()){
                binding.etBankName.error = "Field Required"
                return@setOnClickListener
            }
            if(accountOpeningDate.isNullOrEmpty()){
                binding.etAccountOpeningDate.error = "Field Required"
                return@setOnClickListener
            }
            if(accountNumber.isNullOrEmpty()){
                binding.etAccountNumber.error = "Field Required"
                return@setOnClickListener
            }
            if(bankCode.isNullOrEmpty()){
                binding.etBankCode.error = "Field Required"
                return@setOnClickListener
            }
            lifecycleScope.launch {
                userViewModel.getUserById(userId).observe(this@InfoActivity) { existingUser ->
                    val user = User(
                        name = name.toString(),
                        bankName = bankName.toString(),
                        accountOpeningDate = accountOpeningDate.toString(),
                        accountNumber = accountNumber.toString(),
                        bankCode = bankCode.toString().uppercase(),
                        currentBalance = 0.0,
                        creationDate = existingUser?.creationDate ?: SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
                    )
                    if (existingUser != null) {
                        userViewModel.updateUser(user)
                        fillData(user)
                        Utils.showAlertDialog(
                            this@InfoActivity,
                            Utils.BsType.SUCCESS,
                            "The Data Have Been Updated Successfully",
                            MainActivity::class.java,
                            true
                        )
                    } else {
                        userViewModel.insertUser(user)
                        fillData(user)
                        Utils.showAlertDialog(
                            this@InfoActivity,
                            Utils.BsType.SUCCESS,
                            "The Data Have Been Saved Successfully",
                            MainActivity::class.java,
                            true
                        )
                        val predefinedCategories = listOf(
                            ExpenseCategory(categoryId = 0, userId = 1, categoryName = "Food", categoryDescription = "Expenses for food and dining"),
                            ExpenseCategory(categoryId = 0, userId = 1, categoryName = "Shopping", categoryDescription = "Shopping expenses"),
                            ExpenseCategory(categoryId = 0, userId = 1, categoryName = "Transport", categoryDescription = "Travel and transportation expenses"),
                            ExpenseCategory(categoryId = 0, userId = 1, categoryName = "Utilities", categoryDescription = "Utility bills and services")
                        )
                        for(item in predefinedCategories){
                            expenseCategoryViewModel.insertCategory(item)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillData(user: User) {
        binding.etName.setText(user.name)
        binding.etBankName.setText(user.bankName)
        binding.etAccountOpeningDate.setText(user.accountOpeningDate)
        binding.etAccountNumber.setText(user.accountNumber)
        binding.etBankCode.setText(user.bankCode)
        binding.etCurrentBalance.isEnabled = false

        binding.tvUserName.visibility = View.VISIBLE
        binding.tvBankName.visibility = View.VISIBLE
        binding.tvCurrentBalance.visibility = View.VISIBLE
        binding.tvUserName.text = user.name
        binding.tvBankName.text = user.bankName

        expenseViewModel.getTotalExpenseByUser(1L){ total ->
            totalBalance = total
            if(total == null){
                binding.tvCurrentBalance.text = getString(R.string.rupee_amount) + "0.0"
                binding.etCurrentBalance.setText("0.0")
            } else {
                binding.tvCurrentBalance.text = getString(R.string.rupee_amount) + total.toString()
                binding.etCurrentBalance.setText(total.toString())
            }
        }

        binding.saveButton.text = "UPDATE"
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getUserById(userId).observe(this){ user ->
            user?.let {
                fillData(it)
            }
        }
    }

}