# Java_DSA_ECommerce_CartSystem

A Java Swing-based e-commerce shopping cart system built for the Data Structure & Algorithm (BIC2214) course. The system uses essential data structures to simulate cart management, order processing, and inventory tracking without a backend database.

## 📌 Features
- 🔐 User login and account creation
- 🛍️ Product browsing with categories and images
- 🛒 Cart management using `Stack` (with undo functionality)
- 📦 Order processing via `Queue`
- 🧾 Order storage using `LinkedList`
- 🏷️ Real-time inventory tracking with `HashMap`
- 💾 Data persistence using text files
- ⏱️ Performance benchmarking using `System.nanoTime()`

## 🧠 Data Structures Used
| Data Structure | Purpose                        |
|----------------|--------------------------------|
| Stack          | Cart items & Undo last action  |
| Queue          | Order processing FIFO          |
| LinkedList     | Order item storage             |
| HashMap        | Stock quantity tracking        |

## 🗂️ Project Structure
- `LoginFrame.java` – User authentication interface
- `ProductTreeGUI.java` – Product catalog, cart, and GUI management
- `MainApp.java` – Entry point linking login and shopping interface
- `*.txt` files – Store users, stock, purchases, and order IDs

## 💻 How to Run
1. Open the project in **NetBeans** or any Java IDE.
2. Ensure the `resources` (images) and `.txt` files are in the root directory.
3. Compile and run `MainApp.java`.

## 📸 Screenshort
> See the full demo in the **output** file.

## 📚 References
- NetBeans IDE
- Java Swing Docs
- Course: BIC2214 Data Structure & Algorithm – UCSI University
