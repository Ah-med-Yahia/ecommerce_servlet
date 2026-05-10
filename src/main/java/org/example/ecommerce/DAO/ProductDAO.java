package org.example.ecommerce.DAO;

import org.example.ecommerce.models.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAll(); // for {user,admin}
    Product getById(int id); // for  {user,admin}
    List<Product> getByCategory(int categoryId); // for {user,admin}
    void add(Product product); // for {admin}
    void update(Product product); // for {admin}
    void delete(int id); // for {admin}
}