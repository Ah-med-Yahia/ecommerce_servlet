package org.example.ecommerce.Service.Interfaces;

import org.example.ecommerce.Models.Product;
import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(int id);

    Product addProduct(Product product);

    Product updateProduct(int id, Product product);

    boolean deleteProduct(int id);
}