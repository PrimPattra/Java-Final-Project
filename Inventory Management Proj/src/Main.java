import java.util.*;

import models.Product;
import dao.ProductDAO;
import factory.ProductFactory;
import observer.ConsoleObserver;

public class Main {
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        dao.addObserver(new ConsoleObserver());

        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nInventory Management System");
            System.out.println("1. View Products\n2. Add Product\n3. Update Product\n4. Delete Product\n5. Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        for (Product p : dao.getAllProducts()) {
                            System.out.println(p.getId() + ". " + p.getName() + " | Qty: " + p.getQuantity() + " | Price: $" + p.getPrice());
                        }
                        break;
                    case 2:
                        System.out.print("Product Name: ");
                        String name = sc.nextLine();
                        System.out.print("Category ID: ");
                        int categoryId = sc.nextInt();
                        System.out.print("Supplier ID: ");
                        int supplierId = sc.nextInt();
                        System.out.print("Quantity: ");
                        int quantity = sc.nextInt();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Minimum Quantity: ");
                        int minQty = sc.nextInt();
                        dao.addProduct(ProductFactory.createProduct(0, name, categoryId, supplierId, quantity, price, minQty));
                        break;
                    case 3:
                        System.out.print("Enter Product ID to update: ");
                        int updateId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        String newName = sc.nextLine();
                        System.out.print("Category ID: ");
                        int newCatId = sc.nextInt();
                        System.out.print("Supplier ID: ");
                        int newSupId = sc.nextInt();
                        System.out.print("Quantity: ");
                        int newQty = sc.nextInt();
                        System.out.print("Price: ");
                        double newPrice = sc.nextDouble();
                        System.out.print("Minimum Quantity: ");
                        int newMinQty = sc.nextInt();
                        dao.updateProduct(ProductFactory.createProduct(updateId, newName, newCatId, newSupId, newQty, newPrice, newMinQty));
                        break;
                    case 4:
                        System.out.print("Enter Product ID to delete: ");
                        int deleteId = sc.nextInt();
                        dao.deleteProduct(deleteId);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 5);
    }
}

