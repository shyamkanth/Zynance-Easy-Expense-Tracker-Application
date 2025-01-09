# Zynance : Easy Expense Tracker Application

Zynance is an Android application designed for managing expenses. Users can perform CRUD operations on their expenses and expense categories. Additionally, the app provides various views to track expenses, including bar charts, pie charts, and a tabular view. With support for Room, Coroutines, and the MVVM architecture, this app ensures a clean and efficient approach to managing personal finances.

## Features

- **Create Expenses**: Add new expenses with details like amount, category, and date.
- **Read Expenses**: View all your expenses with their respective details.
- **Update Expenses**: Edit expense details, including amount, category, and date.
- **Delete Expenses**: Remove expenses that are no longer needed.
- **CRUD on Expense Categories**: Create, update, and delete expense categories to organize your expenses.
- **Category-Wise Expense Tracking**: View expenses by category in multiple views:
  - **Bar Chart**: A visual representation of expenses in each category.
  - **Pie Chart**: A breakdown of expenses in each category.
  - **Tabular View**: A detailed tabular listing of expenses, categorized and organized.
  
## Technologies Used

- **Room**: Local database for storing expenses and categories.
- **Coroutines**: For handling asynchronous tasks, such as database operations.
- **MVVM Architecture**: Ensures a clean separation between UI, business logic, and data management.
- **Charts**: Bar and Pie charts for visualizing expenses by category.

## Requirements

- Android 5.0 (Lollipop) or higher.
- Android Studio (for development and building).

## Installation

1. Clone this repository:
    ```bash
    git clone https://github.com/shyamkanth/Zynance-Easy-Expense-Tracker-Application.git
    ```

2. Open the project in Android Studio.

3. Build and run the app on a physical device or emulator.

4. The app should launch and allow you to manage your expenses, view categories, and analyze them through charts.

## Contributing

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details.
