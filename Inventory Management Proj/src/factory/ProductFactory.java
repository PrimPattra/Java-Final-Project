package factory;

import models.Product;

public class ProductFactory {
    public static Product createProduct(int id, String name, int categoryId, int supplierId, int quantity, double price, int minQuantity) {
        return new Product(id, name, categoryId, supplierId, quantity, price, minQuantity);
    }
}