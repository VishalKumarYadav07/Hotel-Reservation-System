# 💰 Personal Expense Tracker — Java

A complete personal finance tracker with both a **Console** interface (beginner-friendly)
and a **JavaFX GUI** (intermediate, dark-themed dashboard).

---

## Project Structure

```
src/expense/
├── Main.java                        ← Entry point (console or GUI)
├── model/
│   ├── Transaction.java             ← Transaction entity (type, category, amount, date)
│   └── Budget.java                  ← Monthly budget limits per category
├── service/
│   └── LedgerService.java           ← All business logic: CRUD, filters, aggregates
├── report/
│   └── ReportGenerator.java         ← Daily / Monthly / Yearly / Range / Full reports
├── ui/
│   ├── console/
│   │   └── ConsoleApp.java          ← Interactive console menu
│   └── fx/
│       └── ExpenseTrackerFX.java    ← JavaFX GUI application
└── util/
    └── Fmt.java                     ← Console formatting helpers
```

---

## Features

| Feature                        | Console | JavaFX GUI |
|-------------------------------|---------|------------|
| Add income / expense           | ✅      | ✅          |
| 15 transaction categories      | ✅      | ✅          |
| View & filter history          | ✅      | ✅          |
| Delete transactions            | ✅      | ✅          |
| Daily report                   | ✅      | —           |
| Monthly report                 | ✅      | ✅ (charts) |
| Yearly report                  | ✅      | ✅ (charts) |
| Custom date range report       | ✅      | —           |
| Budget limits per category     | ✅      | ✅          |
| Budget alerts / warnings       | ✅      | ✅          |
| Bar chart (income vs expense)  | —       | ✅          |
| Pie chart (expense breakdown)  | —       | ✅          |
| Dashboard summary              | ✅      | ✅          |
| Savings rate                   | ✅      | ✅          |
| Demo data (pre-loaded)         | ✅      | ✅          |

---

## Categories

**Income:** Salary, Freelance, Investment, Gift, Other Income

**Expense:** Food & Dining, Rent, Transport, Entertainment, Healthcare,
             Shopping, Utilities, Education, Travel, Other Expense

---

## How to Compile & Run

### Console Mode (no extra dependencies)
```bash
# Compile
mkdir -p out
find src -name "*.java" | grep -v "fx" | xargs javac -d out

# Run
java -cp out expense.ui.console.ConsoleApp
```

### JavaFX GUI Mode
> Requires **JavaFX SDK** (download from https://gluonhq.com/products/javafx/)

```bash
# Compile with JavaFX
javac --module-path /path/to/javafx/lib --add-modules javafx.controls \
      -d out $(find src -name "*.java")

# Run
java --module-path /path/to/javafx/lib --add-modules javafx.controls \
     -cp out expense.ui.fx.ExpenseTrackerFX
```

---

## Console Menu Overview

```
MAIN MENU
  1. ➕ Add Income
  2. ➖ Add Expense
  3. 📋 View Transaction History
  4. 🗑  Delete Transaction
  5. 📊 Reports
      ├─ Daily / Monthly / Yearly
      └─ Custom Date Range / Full History
  6. 🏠 Dashboard Summary
  7. 🎯 Budget Manager
  0. 🚪 Exit
```

---

## JavaFX GUI Tabs

| Tab          | Contents                                           |
|--------------|----------------------------------------------------|
| 🏠 Dashboard  | Stat cards (income, expense, balance, savings %)  |
| ➕ Add Entry  | Form to add income or expense                     |
| 📋 History    | Sortable table with filter + delete               |
| 📊 Charts     | Bar chart (monthly) + Pie chart (categories)      |
| 🎯 Budget     | Set limits + live progress bars                   |
