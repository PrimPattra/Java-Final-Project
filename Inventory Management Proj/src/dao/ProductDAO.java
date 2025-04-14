package dao;

import observer.ConsoleObserver;
import observer.ProductObserver;
import factory.ProductFactory;
import db.DatabaseConnection;
import models.Product;
import java.sql.*;
import java.util.*;

public class ProductDAO {
    private List<ProductObserver> observers = new ArrayList<>();

    public void addObserver(ProductObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (ProductObserver observer : observers) {
            observer.update(message);
        }
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, category_id, supplier_id, quantity, price, min_quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategoryId());
            stmt.setInt(3, product.getSupplierId());
            stmt.setInt(4, product.getQuantity());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinQuantity());
            stmt.executeUpdate();
            notifyObservers("Product added: " + product.getName());
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(ProductFactory.createProduct(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("category_id"),
                    rs.getInt("supplier_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getInt("min_quantity")
                ));
            }
        }
        return products;
    }

    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name=?, category_id=?, supplier_id=?, quantity=?, price=?, min_quantity=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategoryId());
            stmt.setInt(3, product.getSupplierId());
            stmt.setInt(4, product.getQuantity());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinQuantity());
            stmt.setInt(7, product.getId());
            stmt.executeUpdate();
            notifyObservers("Product updated: " + product.getName());
        }
    }

    public void deleteProduct(int id) throws SQLException {
        String query = "DELETE FROM products WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            notifyObservers("Product deleted with ID: " + id);
        }
    }
}
