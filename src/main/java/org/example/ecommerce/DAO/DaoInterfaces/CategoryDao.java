package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.Category;

import java.util.List;

public interface CategoryDao {

    List<Category> findAll();

    Category findById(int id);

    Category findByName(String name);

    Category save(Category category);

    Category update(Category category);

    boolean delete(int id);
}
