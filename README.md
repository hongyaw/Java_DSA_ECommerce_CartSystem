# Java_DSA_ECommerce_CartSystem

A Java Swing-based e-commerce shopping cart system developed for the **Data Structure & Algorithm (BIC2214)** course at UCSI University.

This application demonstrates how core data structures can be applied to simulate an e-commerce platform without using any backend database.

---

## 📁 Files in This Repository

| File Name            | Description |
|---------------------|-------------|
| `LoginFrame.java`   | GUI and logic for user login and account creation |
| `MainApp.java`      | Entry point of the application; launches the login GUI |
| `ProductTreeGUI.java` | Main GUI for browsing products, managing cart, and placing orders |
| `DSA_ECommerce.zip` | Contains the full project source code and data files (e.g., `users.txt`, `stock.txt`, `purchases.txt`) |
| `resources.zip`     | Contains product image resources used by the GUI |
| `output.pdf`        | Output or report file (e.g., screenshots or result documentation) |
| `README.md`         | This readme file |

---

## 📌 Features
- 🔐 User login and registration
- 🛍️ Product browsing with images and categories
- 🛒 Cart operations (Add, Remove, Undo) using `Stack`
- 📦 Order queue management using `Queue`
- 🧾 Order data stored in `LinkedList`
- 🏷️ Real-time inventory using `HashMap`
- 💾 Text file-based persistence (`users.txt`, `stock.txt`, `purchases.txt`)
- ⏱️ Performance benchmarks using `System.nanoTime()`

---

## 🧠 Data Structures Overview

| Data Structure       | Purpose                           |
|----------------------|-----------------------------------|
| `Stack<String>`      | Cart items and undo functionality |
| `Queue<Order>`       | Order processing in FIFO manner   |
| `LinkedList<CartItem>` | Dynamic order item list         |
| `HashMap<String, Integer>` | Inventory stock tracking    |

---

## 🚀 How to Run

1. Open in **NetBeans** or any compatible Java IDE.
2. Extract both `DSA_ECommerce.zip` and `resources.zip` into your working directory.
3. Ensure image files are in `/src/resources/`.
4. Compile and run `MainApp.java`.

---

## ⚠️ Disclaimer

📢 **Academic Use Only**

This project was developed by UCSI students as part of the **BIC2214 coursework**.  
It is provided for **learning, demonstration, and portfolio purposes only**.

🚫 Do **not** reuse, submit, or copy this project for academic credit at any institution.

---

## 📚 References
- Java Swing Documentation
- Apache NetBeans IDE
- Course: Data Structure & Algorithm (BIC2214)
