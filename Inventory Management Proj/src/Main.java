import java.util.*;

import java.awt.GridLayout;
import javax.swing.*;

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
                            int index = 1;
                            for (Product p : products) {
                                sb.append(index++)
                                .append(". ")
                                .append(p.getName())
                                .append(" | ID: ").append(p.getId()) 
                                .append(" | Qty: ").append(p.getQuantity())
                                .append(" | Price: $").append(p.getPrice())
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
                        List<Product> updateCandidates = dao.getAllProducts();
                        if (updateCandidates.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No products to update.");
                            break;
                        }


                        int updateId = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID to update:"));
                        Product selected = null;
                        for (Product p : updateCandidates) {
                            if (p.getId() == updateId) {
                                selected = p;
                                break;
                            }
                        }
                        if (selected == null) {
                            JOptionPane.showMessageDialog(null, "Product not found.");
                            break;
                        }

                        JCheckBox nameBox = new JCheckBox("Update Name");
                        JTextField nameField = new JTextField(selected.getName());
                        nameField.setEnabled(false);

                        JCheckBox qtyBox = new JCheckBox("Update Quantity");
                        JTextField qtyField = new JTextField(String.valueOf(selected.getQuantity()));
                        qtyField.setEnabled(false);

                        JCheckBox priceBox = new JCheckBox("Update Price");
                        JTextField priceField = new JTextField(String.valueOf(selected.getPrice()));
                        priceField.setEnabled(false);

                        nameBox.addActionListener(e -> nameField.setEnabled(nameBox.isSelected()));
                        qtyBox.addActionListener(e -> qtyField.setEnabled(qtyBox.isSelected()));
                        priceBox.addActionListener(e -> priceField.setEnabled(priceBox.isSelected()));

                        JPanel panel = new JPanel(new GridLayout(0, 1));
                        panel.add(nameBox);
                        panel.add(nameField);
                        panel.add(qtyBox);
                        panel.add(qtyField);
                        panel.add(priceBox);
                        panel.add(priceField);

                        int result = JOptionPane.showConfirmDialog(null, panel, "Update Product",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.OK_OPTION) {
                            String newName = selected.getName();
                            int newQty = selected.getQuantity();
                            double newPrice = selected.getPrice();

                            if (nameBox.isSelected()) newName = nameField.getText();
                            if (qtyBox.isSelected()) newQty = Integer.parseInt(qtyField.getText());
                            if (priceBox.isSelected()) newPrice = Double.parseDouble(priceField.getText());

                            dao.updateProduct(ProductFactory.createProduct(
                                selected.getId(), newName, selected.getCategoryId(), selected.getSupplierId(),
                                newQty, newPrice, selected.getMinQuantity()
                            ));
                            String updatedMsg = "Product updated successfully!\n\n" +
                                "ID: " + selected.getId() + "\n" +
                                "Name: " + newName + "\n" +
                                "Category ID: " + selected.getCategoryId() + "\n" +
                                "Supplier ID: " + selected.getSupplierId() + "\n" +
                                "Quantity: " + newQty + "\n" +
                                "Price: $" + newPrice + "\n" +
                                "Minimum Qty: " + selected.getMinQuantity();

                            JOptionPane.showMessageDialog(null, updatedMsg, "Updated Product", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case 4:
                        List<Product> allProducts = dao.getAllProducts();
                        if (allProducts.isEmpty()){
                            JOptionPane.showMessageDialog(null, "No products to delete.");
                            break;
                        }
                        int deleteId = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID to delete:"));
                        Product productToDelete = null;
                        for (Product p : allProducts) {
                            if (p.getId() == deleteId) {
                                productToDelete = p;
                                break;
                            }
                        }
                        String confirmMsg = "Are you sure you want to delete the following product?\n\n" +
                        "ID: " + productToDelete.getId() + "\n" +
                        "Name: " + productToDelete.getName() + "\n" +
                        "Quantity: " + productToDelete.getQuantity() + "\n" +
                        "Price: $" + productToDelete.getPrice();
                        int confirm = JOptionPane.showConfirmDialog(null, confirmMsg, "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            dao.deleteProduct(deleteId);
                        }
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

