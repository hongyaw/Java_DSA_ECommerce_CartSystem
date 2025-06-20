/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dsa_ecommerce;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;



class CartItem {
    private final String name;
    private final double price;
    private int quantity;

    public CartItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decrementQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        } else {
            this.quantity = 0;
        }
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    @Override
    public String toString() {
        return name + " (x" + quantity + ") - RM" + String.format("%.2f", price * quantity);
    }
}

class Order {
    private static int idCounter = loadOrderId(); // Load last used order ID
    final int orderId;
    private final LinkedList<CartItem> items; // Using LinkedList instead of ArrayList
    private final double totalAmount;

    public Order(LinkedList<CartItem> cartItems) {
        this.orderId = idCounter++;
        this.items = new LinkedList<>(cartItems);
        this.totalAmount = calculateTotal();
        saveOrderId(); // Save updated order ID after every order
    }

    private double calculateTotal() {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getPrice() * item.getQuantity();
        }
        return sum;
    }

    private static int loadOrderId() {
        File purchaseFile = new File("purchases.txt");

        if (!purchaseFile.exists() || purchaseFile.length() == 0) {
            return 1;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("order_id.txt"))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            return 1;
        }
    }

    private static void saveOrderId() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order_id.txt"))) {
            writer.write(String.valueOf(idCounter));
        } catch (IOException e) {
            // Silent fail
        }
    }

    public LinkedList<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
               ", Total Amount: RM" + String.format("%.2f", totalAmount) +
               ", Items: " + items;
    }
}


public class ProductTreeGUI extends JFrame {
    private final JTree productTree;
    private final DefaultTreeModel treeModel;
    private final JTextArea cartArea;
    private final JButton addToCartButton, removeFromCartButton, undoRemoveButton, viewCartButton, checkoutButton, viewOrdersButton;
    private final JPanel productImagePanel;
    private final Stack<String> cartStack;
    private String selectedItem;
    private final Stack<String> undoStack = new Stack<>();
    private final List<Order> orders = new ArrayList<>();
    private final Queue<Order> orderQueue = new LinkedList<>();
    private final String currentUsername;
    private final int INITIAL_QUANTITY = 200; // Each product starts with 200 quantity
    private final Map<String, Integer> productStock = new HashMap<>();

    // Define items and their corresponding price tiers
    private final String[] badmintonItems = {"Badminton Shirt", "Badminton Pants", "Badminton Shoe", "Badminton Racquet", "Shuttlecock", "Badminton Socks"};
    private final double[][] prices = {
        {50.0, 45.0, 40.0, 38.0, 35.0},  // Badminton Shirt
        {40.0, 35.0, 30.0, 28.0, 25.0},  // Badminton Pants
        {120.0, 110.0, 100.0, 90.0, 85.0}, // Badminton Shoe
        {200.0, 190.0, 180.0, 170.0, 160.0}, // Badminton Racquet
        {30.0, 28.0, 25.0, 22.0, 20.0},  // Shuttlecock
        {20.0, 18.0, 16.0, 15.0, 12.0}   // Badminton Socks
    };
    public ProductTreeGUI(String username) {
        this.currentUsername = username;
        setTitle("Product Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());

        cartStack = new Stack<>();

        // Load stock from file instead of resetting to 50
        loadStockFromFile();  

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Products");
        createProductTree(rootNode);
        treeModel = new DefaultTreeModel(rootNode);
        productTree = new JTree(treeModel);
        productTree.addTreeSelectionListener(e -> displayProductImages());

        JScrollPane treeScrollPane = new JScrollPane(productTree);
        cartArea = new JTextArea(20, 50);
        cartArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartArea);

        productImagePanel = new JPanel();
        productImagePanel.setLayout(new GridLayout(1, 5));
        productImagePanel.setPreferredSize(new Dimension(800, 400));

        addToCartButton = new JButton("Add to Cart");
        removeFromCartButton = new JButton("Remove from Cart");
        undoRemoveButton = new JButton("Undo Remove");
        viewCartButton = new JButton("View Cart");
        checkoutButton = new JButton("Checkout");
        viewOrdersButton = new JButton("View Orders");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        buttonPanel.add(removeFromCartButton);
        buttonPanel.add(undoRemoveButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(viewOrdersButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(productImagePanel, BorderLayout.NORTH);
        centerPanel.add(cartScrollPane, BorderLayout.SOUTH);

        add(treeScrollPane, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        undoRemoveButton.addActionListener(e -> undoRemove());
        viewCartButton.addActionListener(e -> viewCart());
        checkoutButton.addActionListener(e -> checkout());
        viewOrdersButton.addActionListener(e -> viewOrders());
}


    private void createProductTree(DefaultMutableTreeNode rootNode) {
        DefaultMutableTreeNode badmintonNode = new DefaultMutableTreeNode("Badminton Accessories");

        for (String item : badmintonItems) {
            DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(item);
            badmintonNode.add(itemNode);

        // Initialize stock ONLY if it's missing in the HashMap
        for (int i = 1; i <= 5; i++) {
            String variantName = item + " (Image " + i + ")";
            productStock.putIfAbsent(variantName, INITIAL_QUANTITY); // ✅ Only initialize if not already in stock.txt
        }
    }
    rootNode.add(badmintonNode);
}

    private void displayProductImages() {
        TreePath selectedPath = productTree.getSelectionPath();
        if (selectedPath == null) return;
        selectedItem = selectedPath.getLastPathComponent().toString();

        productImagePanel.removeAll();
        JLabel[] imageLabels = new JLabel[5]; // Store references to labels for resetting borders

        for (int i = 0; i < 5; i++) {
            String imageVariant = selectedItem + " (Image " + (i + 1) + ")";
            String imagePath = "/resources/" + selectedItem.replace(" ", "_").toLowerCase() + "_" + (i + 1) + ".jpg";
            java.net.URL imgUrl = getClass().getResource(imagePath);

            if (imgUrl == null) {
                System.out.println("Error loading image: " + imagePath);
                continue;
            }

            // Load and scale image
            ImageIcon icon = new ImageIcon(imgUrl);
            JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Default border

            // Store reference for resetting
            imageLabels[i] = imageLabel;

            // Retrieve stock for the specific image
            int stock = productStock.getOrDefault(imageVariant, 0);

            // Create a panel to hold the image, stock, and price label
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());
            imagePanel.add(imageLabel, BorderLayout.CENTER);

            // Stock label above image (now per image)
            JLabel stockLabel = new JLabel("Stock: " + stock, SwingConstants.CENTER);
            stockLabel.setFont(new Font("Arial", Font.BOLD, 12));
            imagePanel.add(stockLabel, BorderLayout.NORTH);

            // Price label under the image
            double price = getPrice(imageVariant);
            JLabel priceLabel = new JLabel("RM" + String.format("%.2f", price), SwingConstants.CENTER);
            imagePanel.add(priceLabel, BorderLayout.SOUTH);

            // Make image clickable
            final int index = i;
            imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // Reset all borders
                    for (JLabel label : imageLabels) {
                        if (label != null) {
                            label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Reset to default
                        }
                    }
                    // Highlight selected image
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                    selectedItem = selectedPath.getLastPathComponent().toString() + " (Image " + (index + 1) + ")";
                }
            });

        productImagePanel.add(imagePanel);
    }

    productImagePanel.revalidate();
    productImagePanel.repaint();
}


    private void viewCart() {
        if (cartStack.isEmpty()) {
            cartArea.setText("Cart is empty.\n");
        } else {
            double totalPrice = 0.0;
            StringBuilder cartContent = new StringBuilder();

            for (String item : cartStack) {
                double price = getPrice(item);
                cartContent.append(item) // ✅ Ensure correct name is displayed
                            .append(" - RM")
                            .append(String.format("%.2f", price))
                            .append("\n");
                totalPrice += price;
            }

        cartContent.append("\nTotal Price: RM").append(String.format("%.2f", totalPrice));
        cartArea.setText(cartContent.toString());
    }
}




    private void addToCart() {
        if (selectedItem != null) {
            // Prevent adding categories
            if (!selectedItem.contains("(Image")) {
                JOptionPane.showMessageDialog(this, "Please select a specific item with an image.");
                return;
            }

            int currentStock = productStock.getOrDefault(selectedItem, 0);

            // Ask user for quantity
            String quantityStr = JOptionPane.showInputDialog(this, 
                "Enter quantity for " + selectedItem + " (Available: " + currentStock + "):");

            if (quantityStr == null) return;

            try {
                int quantity = Integer.parseInt(quantityStr);

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be at least 1.");
                    return;
                }

                if (quantity > currentStock) {
                    JOptionPane.showMessageDialog(this, "Not enough stock available!");
                    return;
                }

                // Deduct stock & Save
                productStock.put(selectedItem, currentStock - quantity);


                // Add multiple items
                for (int i = 0; i < quantity; i++) {
                    cartStack.push(selectedItem);
                }

                refreshCartDisplay();
                displayProductImages();
                JOptionPane.showMessageDialog(this, selectedItem + " (x" + quantity + ") added to cart. Remaining stock: " + (currentStock - quantity));

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item before adding to cart.");
        }
    }




    private void removeFromCart() {
        if (!cartStack.isEmpty()) {
            String removedItem = cartStack.pop();
            undoStack.push(removedItem); // Store for undo

            // Restore stock by 1 (only for the removed item)
            int currentStock = productStock.getOrDefault(removedItem, 0);
            productStock.put(removedItem, currentStock + 1);

            refreshCartDisplay();
            displayProductImages(); // Update UI stock count
            JOptionPane.showMessageDialog(this, removedItem + " removed from cart. Stock restored: " + (currentStock + 1));
        } else {
            JOptionPane.showMessageDialog(this, "Cart is empty. Nothing to remove.");
        }
    }

    private void undoRemove() {
        if (!undoStack.isEmpty()) {
            String restoredItem = undoStack.pop();
            cartStack.push(restoredItem); // Restore item back to cart

            // Reduce stock again
            int currentStock = productStock.getOrDefault(restoredItem, 0);
            if (currentStock > 0) {
                productStock.put(restoredItem, currentStock - 1);
                JOptionPane.showMessageDialog(this, "Restored: " + restoredItem + ". Stock reduced: " + (currentStock - 1));
            } else {
                JOptionPane.showMessageDialog(this, "Stock already at zero. Cannot reduce further.");
            }

            refreshCartDisplay();
            displayProductImages(); // Update UI stock count
        } else {
            JOptionPane.showMessageDialog(this, "No undo available.");
    }
}

    private void checkout() {
        if (cartStack.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty. Add items before checkout.");
            return;
        }

        List<CartItem> cartItems = new ArrayList<>();
        while (!cartStack.isEmpty()) {
            String itemName = cartStack.pop();
            double price = getPrice(itemName);

            cartItems.add(new CartItem(itemName, price, 1));
        }

        Order newOrder = new Order(new LinkedList<>(cartItems));
        orders.add(newOrder);
        orderQueue.offer(newOrder); 


        savePurchaseToFile(currentUsername, newOrder);
        saveStockToFile(); // Save stock levels after checkout

        JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + newOrder.toString());
        refreshCartDisplay();
    }



    private void saveStockToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("stock.txt"))) {
                for (Map.Entry<String, Integer> entry : productStock.entrySet()) {
                    writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving stock data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
 
    private void savePurchaseToFile(String username, Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("purchases.txt", true))) {
            writer.write("Order ID: " + order.orderId + "\n");
            writer.write("Username: " + username + "\n");
            writer.write("Order Details: " + order.toString() + "\n");
            writer.write("----------------------\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving purchase.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void refreshCartDisplay() {
        cartArea.setText("");
        for (String item : cartStack) {
            cartArea.append(item + "\n");
        }
    }
    private double getPrice(String selectedItem) {
    // Extract base item name (remove " (Image X)" if exists)
        String itemName = selectedItem.split(" \\(Image")[0];

        for (int i = 0; i < badmintonItems.length; i++) {
            if (badmintonItems[i].equals(itemName)) {
                // Default to first price if no specific image is selected
                int priceIndex = 0;

                // Check if an image index was selected
                if (selectedItem.contains("(Image ")) {
                    try {
                        // Extract image index from name
                        priceIndex = Integer.parseInt(selectedItem.replaceAll("[^0-9]", "")) - 1;
                        priceIndex = Math.max(0, Math.min(priceIndex, prices[i].length - 1)); // Ensure index is within bounds
                    } catch (NumberFormatException e) {
                        priceIndex = 0;
                    }
                }

                return prices[i][priceIndex]; // Return corresponding price
            }
        }
        return 50.00; // Default price if not found
    }
    private void viewOrders() {
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders have been placed yet.");
            return;
        }

        StringBuilder orderHistory = new StringBuilder("Order History:\n");

        for (Order order : orders) {
            orderHistory.append(order.toString()).append("\n");
        }

        JOptionPane.showMessageDialog(this, orderHistory.toString());
    }

private void loadStockFromFile() {
    File file = new File("stock.txt");

    if (!file.exists()) {
        // If no file exists, initialize stock but DO NOT overwrite existing data
        for (String item : badmintonItems) {
            for (int i = 1; i <= 5; i++) {
                String variantName = item + " (Image " + i + ")";
                productStock.put(variantName, INITIAL_QUANTITY);
            }
        }
        saveStockToFile(); // ✅ Save only if stock file was missing
        return;
    }

    // Read existing stock from file
    try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                productStock.put(parts[0], Integer.valueOf(parts[1]));
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading stock data.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
//Test add and remove 100 and 150 items
/*
private void runPerformanceTest() {
    // Test: Add 100 items
    long startAdd100 = System.nanoTime();
    for (int i = 0; i < 100; i++) {
        cartStack.push("Badminton Shirt (Image 1)");
    }
    long endAdd100 = System.nanoTime();
    System.out.println("Time to add 100 items: " + (endAdd100 - startAdd100) + " ns");

    // Test: Remove 100 items
    long startRemove100 = System.nanoTime();
    for (int i = 0; i < 100; i++) {
        if (!cartStack.isEmpty()) {
            cartStack.pop();
        }
    }
    long endRemove100 = System.nanoTime();
    System.out.println("Time to remove 100 items: " + (endRemove100 - startRemove100) + " ns");

    // Add 150 items
    long startAdd150 = System.nanoTime();
    for (int i = 0; i < 150; i++) {
        cartStack.push("Badminton Shirt (Image 1)");
    }
    long endAdd150 = System.nanoTime();
    System.out.println("Time to add 150 items: " + (endAdd150 - startAdd150) + " ns");

    // Remove 150 items
    long startRemove150 = System.nanoTime();
    for (int i = 0; i < 150; i++) {
        if (!cartStack.isEmpty()) {
            cartStack.pop();
        }
    }
    long endRemove150 = System.nanoTime();
    System.out.println("Time to remove 150 items: " + (endRemove150 - startRemove150) + " ns");
}
*/

//Test LoadStockFromFile()
/*
public void runLoadStockBenchmark() {
    // Step 1: Simulate custom stock generation
    long startGenerate = System.nanoTime();
    for (int i = 0; i < 20; i++) {
        for (int j = 1; j <= 5; j++) {
            String variant = "Product " + i + " (Image " + j + ")";
            productStock.put(variant, 10); // Simulate HashMap.put()
        }
    }
    long endGenerate = System.nanoTime();
    long generateTime = endGenerate - startGenerate;

    // Step 2: Simulate reading + parsing + processing
    long startProcess = System.nanoTime();
    for (int i = 0; i < 100; i++) {
        String line = "Product " + i + " (Image 1): 10";
        String[] parts = line.split(": ");
        if (parts.length == 2) {
            int quantity = Integer.parseInt(parts[1]);
            productStock.put(parts[0], quantity);
        }
    }
    long endProcess = System.nanoTime();
    long processTime = endProcess - startProcess;

    // Step 3: Simulate saving to file
    long startSave = System.nanoTime();
    for (int i = 0; i < 100; i++) {
        // simulate writing line to file
        String line = "Product " + i + ": 10";
    }
    long endSave = System.nanoTime();
    long saveTime = endSave - startSave;

    long total = generateTime + processTime + saveTime;

    // Print result
    System.out.println("Step 1 - Generate stock time: " + generateTime + " ns");
    System.out.println("Step 2 - Read + Process time: " + processTime + " ns");
    System.out.println("Step 3 - Save to file time: " + saveTime + " ns");
    System.out.println("Total simulated worst-case time: " + total + " ns");
}
*/


public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductTreeGUI frame = new ProductTreeGUI("Guest");
            frame.setVisible(true);
        });
    }

}

