package org.example.ecommerce.Service.Interfaces;

import org.example.ecommerce.Models.Category;

import java.util.List;

public interface CategoryService {


    List<Category> getAllCategories();

    Category getCategoryById(int id);

    Category addCategory(Category category);

    Category updateCategory(int id, Category category);

    boolean deleteCategory(int id);
}
