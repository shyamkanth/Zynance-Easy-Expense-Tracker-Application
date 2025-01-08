package io.github.shyamkanth.zynance.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import io.github.shyamkanth.zynance.R
import io.github.shyamkanth.zynance.adapter.CategoryAdapter
import io.github.shyamkanth.zynance.databinding.AddCategoryLayoutBinding
import io.github.shyamkanth.zynance.databinding.AddExpenseLayoutBinding
import io.github.shyamkanth.zynance.databinding.CustomDialogLayoutBinding
import io.github.shyamkanth.zynance.databinding.ManageCategoriesDialogLayoutBinding
import io.github.shyamkanth.zynance.databinding.PopupLayoutBinding
import io.github.shyamkanth.zynance.modal.entity.Expense
import io.github.shyamkanth.zynance.modal.entity.ExpenseCategory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object Utils {

    enum class BsType {
        INFO,
        SUCCESS,
        ERROR
    }

    enum class AddType {
        EXPENSE,
        CATEGORY
    }

    private val colorArray = intArrayOf(
        R.color.purple_E9CAF8,
        R.color.light_blue_CAE7F8,
        R.color.light_green_D0F8CA,
        R.color.light_pink_F8D2CA
    )

    fun showDatePicker(editText: EditText, context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                editText.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showAlertDialogInSameActivity(
        currentActivity: Activity,
        dialogType: BsType,
        message: String,
    ) {
        val binding = CustomDialogLayoutBinding.inflate(currentActivity.layoutInflater)
        when (dialogType) {
            BsType.SUCCESS -> {
                binding.imageViewSuccess.setImageResource(R.drawable.ic_success)
                binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary))
                binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary))
                binding.textViewMessage.text = "Success!"
            }

            BsType.INFO -> {
                binding.imageViewSuccess.setImageResource(R.drawable.ic_info)
                binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary_info))
                binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary_info))
                binding.textViewMessage.text = "Info!"
            }

            BsType.ERROR -> {
                binding.imageViewSuccess.setImageResource(R.drawable.ic_error)
                binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary_error))
                binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary_error))
                binding.textViewMessage.text = "Error!"
            }
        }
        binding.textViewDescription.text = message
        binding.buttonOk.text = "OK"
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.buttonOk.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showAlertDialog(
        currentActivity: Activity,
        dialogType: BsType,
        message: String,
        targetActivity: Class<*>,
        finishActivity: Boolean = false
    ) {
        val binding = CustomDialogLayoutBinding.inflate(currentActivity.layoutInflater)
        when (dialogType) {
            BsType.SUCCESS -> {
                binding.imageViewSuccess.setImageResource(R.drawable.ic_success)
                binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary))
                binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary))
                binding.textViewMessage.text = "Success!"
            }

            BsType.INFO -> {
                binding.imageViewSuccess.setImageResource(R.drawable.ic_info)
                binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary_info))
                binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary_info))
                binding.textViewMessage.text = "Info!"
            }

            BsType.ERROR -> {
                binding.imageViewSuccess.setImageResource(R.drawable.ic_error)
                binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary_error))
                binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary_error))
                binding.textViewMessage.text = "Error!"
            }
        }
        binding.textViewDescription.text = message
        binding.buttonOk.text = "OK"
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.buttonOk.setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent(currentActivity, targetActivity).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            currentActivity.startActivity(intent)
            if (finishActivity) {
                currentActivity.finish()
            }
        }

        alertDialog.show()
    }

    fun showDeleteDialog(
        currentActivity: Activity,
        onDelete: () -> Unit
    ) {
        val binding = CustomDialogLayoutBinding.inflate(currentActivity.layoutInflater)
        binding.imageViewSuccess.setImageResource(R.drawable.ic_error)
        binding.textViewMessage.setTextColor(currentActivity.getColor(R.color.primary_error))
        binding.buttonOk.setBackgroundColor(currentActivity.getColor(R.color.primary_error))
        binding.textViewMessage.text = "DELETE"
        binding.textViewDescription.text = "This action will delete this information and all its associated data. Are you sure you want to delete?"
        binding.buttonOk.visibility = View.GONE
        binding.buttonCancel.visibility = View.VISIBLE
        binding.buttonCancel.text = "CANCEL"
        binding.buttonDelete.visibility = View.VISIBLE
        binding.buttonDelete.text = "DELETE"
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.buttonDelete.setOnClickListener {
            onDelete()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun showPopupAboveButton(
        currentActivity: Activity,
        button: View,
        onPopupAction: (AddType) -> Unit
    ) {
        val binding = PopupLayoutBinding.inflate(currentActivity.layoutInflater)
        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val location = IntArray(2)
        button.getLocationOnScreen(location)

        val popupX = location[0] - button.width * 2
        val popupY = location[1] - button.height * 3

        popupWindow.showAtLocation(button, Gravity.NO_GRAVITY, popupX, popupY)
        popupWindow.isOutsideTouchable = true
        binding.tvAddExpense.setOnClickListener {
            popupWindow.dismiss()
            onPopupAction(AddType.EXPENSE)
        }
        binding.tvAddCategory.setOnClickListener {
            popupWindow.dismiss()
            onPopupAction(AddType.CATEGORY)
        }
    }

    @SuppressLint("SetTextI18n")
    fun showAddExpenseCategoryDialog(
        currentActivity: Activity,
        onSave: (ExpenseCategory) -> Unit
    ) {
        val binding = AddCategoryLayoutBinding.inflate(currentActivity.layoutInflater)
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnAddCategory.setOnClickListener {
            val userId = 1L
            val categoryName = binding.etAddCategoryName.text.toString().trim()
            val categoryDescription = binding.etAddCategoryDescription.text.toString().trim()
            if (categoryName.isEmpty()) {
                binding.etAddCategoryName.error = "Please enter category name"
                return@setOnClickListener
            }
            val expenseCategory = ExpenseCategory(
                categoryId = 0,
                userId = userId,
                categoryName = categoryName,
                categoryDescription = categoryDescription
            )
            onSave(expenseCategory)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showEditExpenseCategoryDialog(
        currentActivity: Activity,
        expenseCategory: ExpenseCategory,
        onSave: (ExpenseCategory) -> Unit
    ) {
        val binding = AddCategoryLayoutBinding.inflate(currentActivity.layoutInflater)
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.etAddCategoryName.setText(expenseCategory.categoryName)
        binding.etAddCategoryDescription.setText(expenseCategory.categoryDescription)
        binding.btnAddCategory.text = "Update"
        binding.dialogTitle.text = "Update Category"
        binding.btnAddCategory.setOnClickListener {
            val userId = 1L
            val categoryName = binding.etAddCategoryName.text.toString().trim()
            val categoryDescription = binding.etAddCategoryDescription.text.toString().trim()
            if (categoryName.isEmpty()) {
                binding.etAddCategoryName.error = "Please enter category name"
                return@setOnClickListener
            }
            val expenseCategoryNew = ExpenseCategory(
                categoryId = expenseCategory.categoryId,
                userId = userId,
                categoryName = categoryName,
                categoryDescription = categoryDescription
            )
            onSave(expenseCategoryNew)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showAddExpenseDialog(
        currentActivity: Activity,
        spinnerItem: List<ExpenseCategory>,
        onSave: (Expense) -> Unit
    ) {
        val binding = AddExpenseLayoutBinding.inflate(currentActivity.layoutInflater)
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        val listItem = mutableListOf<String>()
        if(spinnerItem.isEmpty()){
            listItem.add(0, "No Category Added")
        } else {
            listItem.add(0, "Select Category")
            for (item in spinnerItem) {
                listItem.add(item.categoryName ?: "")
            }
        }
        val spinner = binding.spinnerCategoryName
        val adapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_item, listItem)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        binding.spinnerCategoryName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 != 0) {
                        binding.tvErrorCategory.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        binding.etExpenseDate.setOnClickListener {
            showDatePicker(binding.etExpenseDate, currentActivity)
        }
        binding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnAddExpense.setOnClickListener {
            val userId = 1L
            val expenseAmount = binding.etExpenseAmount.text.toString().trim()
            val expenseDate = binding.etExpenseDate.text.toString().trim()
            val description = binding.etAddCategoryDescription.text.toString().trim()
            val selectedCategory = binding.spinnerCategoryName.selectedItem.toString()
            val expenseCategory = spinnerItem.find {
                it.categoryName == selectedCategory
            }
            if (binding.spinnerCategoryName.selectedItemPosition == 0) {
                binding.tvErrorCategory.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (expenseAmount.isEmpty()) {
                binding.etExpenseAmount.error = "Please enter expense amount"
                return@setOnClickListener
            }
            if (expenseDate.isEmpty()) {
                binding.etExpenseDate.error = "Please select expense date"
                return@setOnClickListener
            }
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val currentDateTime = "$expenseDate  $currentTime"
            val expense = Expense(
                userId = userId,
                categoryId = expenseCategory?.categoryId,
                expenseAmount = expenseAmount.toDouble(),
                description = description.takeIf { it.isNotEmpty() },
                expenseDate = currentDateTime,
            )
            onSave(expense)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showAddExpenseDialogWithDetail(
        currentActivity: Activity,
        expense: Expense,
        spinnerItem: List<ExpenseCategory>,
        onSave: (Expense, Boolean) -> Unit
    ) {
        val binding = AddExpenseLayoutBinding.inflate(currentActivity.layoutInflater)
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        val listItem = mutableListOf<String>()
        if(spinnerItem.isEmpty()){
            listItem.add(0, "No Category Added")
        } else {
            listItem.add(0, "Select Category")
            for (item in spinnerItem) {
                listItem.add(item.categoryName ?: "")
            }
        }
        val spinner = binding.spinnerCategoryName
        val adapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_item, listItem)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        binding.spinnerCategoryName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 != 0) {
                        binding.tvErrorCategory.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        if(expense.description != null){
            binding.etAddCategoryDescription.setText(expense.description)
        }
        val selectedCategory = spinnerItem.find {
            it.categoryId == expense.categoryId
        }
        if(selectedCategory != null){
            binding.spinnerCategoryName.setSelection(listItem.indexOf(selectedCategory.categoryName))
        }
        binding.etExpenseAmount.setText(expense.expenseAmount.toString())
        binding.etExpenseDate.setText(expense.expenseDate?.take(10))
        binding.etAddCategoryDescription.setText(expense.description)
        binding.dialogTitle.text = "Update Expense"
        binding.etExpenseDate.setOnClickListener {
            showDatePicker(binding.etExpenseDate, currentActivity)
        }
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnDelete.setOnClickListener {
            onSave(expense, true)
            alertDialog.dismiss()
        }
        binding.btnAddExpense.text = "UPDATE"
        binding.btnAddExpense.setOnClickListener {
            val userId = 1L
            val expenseAmount = binding.etExpenseAmount.text.toString().trim()
            val expenseDate = binding.etExpenseDate.text.toString().trim()
            val description = binding.etAddCategoryDescription.text.toString().trim()
            val selectedCategoryNew = binding.spinnerCategoryName.selectedItem.toString()
            val expenseCategory = spinnerItem.find {
                it.categoryName == selectedCategoryNew
            }
            if (binding.spinnerCategoryName.selectedItemPosition == 0) {
                binding.tvErrorCategory.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (expenseAmount.isEmpty()) {
                binding.etExpenseAmount.error = "Please enter expense amount"
                return@setOnClickListener
            }
            if (expenseDate.isEmpty()) {
                binding.etExpenseDate.error = "Please select expense date"
                return@setOnClickListener
            }
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val currentDateTime = "$expenseDate  $currentTime"
            val expenseNew = Expense(
                expenseId = expense.expenseId,
                userId = userId,
                categoryId = expenseCategory?.categoryId,
                expenseAmount = expenseAmount.toDouble(),
                description = description.takeIf { it.isNotEmpty() },
                expenseDate = currentDateTime,
            )
            onSave(expenseNew, false)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showManageCategoryDialog(
        currentActivity: Activity,
        categoryList: List<ExpenseCategory>
    ) {
        val binding = ManageCategoriesDialogLayoutBinding.inflate(currentActivity.layoutInflater)
        val alertDialog = AlertDialog.Builder(currentActivity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.btnOk.setOnClickListener {
            alertDialog.dismiss()
        }
//        val list = mutableListOf<ExpenseCategory>()
//        for(item in categoryList){
//            list.add(item)
//        }
        val adapter = CategoryAdapter(currentActivity, currentActivity, categoryList as MutableList<ExpenseCategory>)
        binding.rvCategoryList.adapter = adapter
        binding.rvCategoryList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(currentActivity)

        alertDialog.show()
    }

    fun generateImageInitials(
        context: Context,
        name: String,
        position: Int,
        diameter: Int = 100
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val colorResId = colorArray[position % colorArray.size]
        val backgroundColor = ContextCompat.getColor(context, colorResId)
        val paint = Paint().apply {
            isAntiAlias = true
            color = backgroundColor
        }
        val radius = diameter / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        paint.color = ContextCompat.getColor(context, android.R.color.black)
        paint.textSize = diameter / 2.5f
        paint.typeface = Typeface.DEFAULT
        paint.textAlign = Paint.Align.CENTER

        val initials = name.take(2)
        val bounds = Rect()
        paint.getTextBounds(initials, 0, initials.length, bounds)
        val y = radius - (bounds.top + bounds.bottom) / 2f

        canvas.drawText(initials, radius, y, paint)

        return bitmap
    }

    fun generateColorForCategory(categoryId: Long): Int {
        val seed = categoryId.toInt()
        val random = Random(seed)
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)
        return Color.rgb(r, g, b)
    }

    val colors = listOf(
        Color.rgb(66, 133, 244),  // Blue
        Color.rgb(219, 68, 55),   // Red
        Color.rgb(244, 180, 0),   // Yellow
        Color.rgb(15, 157, 88),   // Green
        Color.rgb(255, 87, 34),   // Orange
        Color.rgb(102, 187, 106), // Light Green
        Color.rgb(33, 150, 243),  // Light Blue
        Color.rgb(156, 39, 176),  // Purple
        Color.rgb(255, 193, 7),   // Amber
        Color.rgb(0, 188, 212),   // Cyan
        Color.rgb(121, 85, 72),   // Brown
        Color.rgb(255, 235, 59),  // Bright Yellow
        Color.rgb(103, 58, 183),  // Deep Purple
        Color.rgb(96, 125, 139),  // Greyish Blue
        Color.rgb(76, 175, 80),   // Vibrant Green
        Color.rgb(192, 57, 43),   // Dark Red
        Color.rgb(46, 204, 113),  // Mint Green
        Color.rgb(241, 196, 15),  // Mustard Yellow
        Color.rgb(39, 174, 96),   // Forest Green
        Color.rgb(142, 68, 173),  // Violet
        Color.rgb(44, 62, 80),    // Navy Blue
        Color.rgb(127, 140, 141), // Soft Grey
        Color.rgb(230, 126, 34),  // Pumpkin Orange
        Color.rgb(26, 188, 156),  // Teal
        Color.rgb(52, 152, 219),  // Sky Blue
        Color.rgb(155, 89, 182),  // Amethyst Purple
        Color.rgb(243, 156, 18),  // Carrot Orange
        Color.rgb(211, 84, 0),    // Rust Orange
        Color.rgb(149, 165, 166), // Light Grey
        Color.rgb(22, 160, 133)   // Emerald Green
    )




    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
