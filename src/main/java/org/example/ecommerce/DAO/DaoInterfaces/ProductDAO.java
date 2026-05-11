package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.Product;
import java.util.List;

public interface ProductDAO {

    List<Product> findAll();

    Product findById(int id);

    Product save(Product product);

    Product update(Product product);

    boolean delete(int id);
}