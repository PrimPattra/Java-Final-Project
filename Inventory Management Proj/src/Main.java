import java.util.*;

import javax.swing.JOptionPane;

import models.Product;
import dao.ProductDAO;
import factory.ProductFactory;
import observer.ConsoleObserver;

public class Main {
    public static void main(String[] args) {
        int choice;
        ProductDAO dao = new ProductDAO();
        dao.addObserver(new ConsoleObserver());

        do {
            String input = JOptionPane.showInputDialog(
                null,
                "Inventory Management System\n" +
                "1. View Products\n" +
                "2. Add Product\n" +
                "3. Update Product\n" +
                "4. Delete Product\n" +
                "5. Exit\n\nChoose an option:"
            );

            if (input == null) break; // User press cancel or close the window

            try {
                choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        List<Product> products = dao.getAllProducts();
                        if (products.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No products found.");
                        } else {
                            StringBuilder sb = new StringBuilder("Product List:\n");
                            for (Product p : products) {
                                sb.append(p.getId())
                                .append(". ")
                                .append(p.getName())
                                .append(" | Qty: ")
                                .append(p.getQuantity())
                                .append(" | Price: $")
                                .append(p.getPrice())
                                .append("\n");
                            }
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                        break;

                    case 2:
                        String name = JOptionPane.showInputDialog("Product Name:");
                        int categoryId = Integer.parseInt(JOptionPane.showInputDialog("Category ID:"));
                        int supplierId = Integer.parseInt(JOptionPane.showInputDialog("Supplier ID:"));
                        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
                        double price = Double.parseDouble(JOptionPane.showInputDialog("Price:"));
                        int minQty = Integer.parseInt(JOptionPane.showInputDialog("Minimum Quantity:"));
                        dao.addProduct(ProductFactory.createProduct(0, name, categoryId, supplierId, quantity, price, minQty));
                        break;

                    case 3:
                        int updateId = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID to update:"));
                        String newName = JOptionPane.showInputDialog("New Name:");
                        int newCatId = Integer.parseInt(JOptionPane.showInputDialog("Category ID:"));
                        int newSupId = Integer.parseInt(JOptionPane.showInputDialog("Supplier ID:"));
                        int newQty = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
                        double newPrice = Double.parseDouble(JOptionPane.showInputDialog("Price:"));
                        int newMinQty = Integer.parseInt(JOptionPane.showInputDialog("Minimum Quantity:"));
                        dao.updateProduct(ProductFactory.createProduct(updateId, newName, newCatId, newSupId, newQty, newPrice, newMinQty));
                        break;

                    case 4:
                        int deleteId = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID to delete:"));
                        dao.deleteProduct(deleteId);
                        break;

                    case 5:
                        JOptionPane.showMessageDialog(null, "Goodbye!");
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                choice = -1; // continue the loop
            }

        } while (choice != 5);
    }
}

