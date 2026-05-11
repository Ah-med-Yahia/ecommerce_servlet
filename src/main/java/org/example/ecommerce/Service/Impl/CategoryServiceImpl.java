package org.example.ecommerce.Service.Impl;

import org.example.ecommerce.DAO.DaoImpl.CategoryDaoImpl;
import org.example.ecommerce.DAO.DaoInterfaces.CategoryDao;
import org.example.ecommerce.Models.Category;
import org.example.ecommerce.Service.Interfaces.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryRepository = new CategoryDaoImpl();

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Category addCategory(Category category) {

        if (categoryRepository.findByName(category.getName()) != null) {
            throw new RuntimeException("CATEGORY_ALREADY_EXISTS");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category category) {
        Category existing = categoryRepository.findById(id);
        if (existing == null) return null;
        category.setId(id);
        return categoryRepository.update(category);
    }

    public boolean deleteCategory(int id) {
        return categoryRepository.delete(id);
    }
}
