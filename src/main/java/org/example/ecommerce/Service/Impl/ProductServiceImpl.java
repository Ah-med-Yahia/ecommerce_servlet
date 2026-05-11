package org.example.ecommerce.Service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.Config.AppConfigContext;
import org.example.ecommerce.Models.Product;
import org.example.ecommerce.Service.Interfaces.ProductService;
import org.example.ecommerce.DAO.DaoInterfaces.ProductDAO;
import org.example.ecommerce.DAO.DaoImpl.ProductDaoImpl;
import org.example.ecommerce.Utils.RedisUtil;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private static final String CACHE_KEY = "products:all";
    private static final int CACHE_TTL = 300; // 5 minutes

    private final ProductDAO productRepository = new ProductDaoImpl();
    private final ObjectMapper mapper =
            (ObjectMapper) AppConfigContext.getContext().getAttribute("objectMapper");

    @Override
    public List<Product> getAllProducts() {
        try {
            String cached = RedisUtil.get(CACHE_KEY);
            if (cached != null) {
                return mapper.readValue(cached, new TypeReference<List<Product>>() {});
            }
        } catch (Exception ignored) {}

        List<Product> products = productRepository.findAll();

        try {
            RedisUtil.saveWithTTL(CACHE_KEY, mapper.writeValueAsString(products), CACHE_TTL);
        } catch (Exception ignored) {}

        return products;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public Product addProduct(Product product) {
        Product saved = productRepository.save(product);
        RedisUtil.delete(CACHE_KEY);
        return saved;
    }

    @Override
    public Product updateProduct(int id, Product product) {
        Product existing = productRepository.findById(id);
        if (existing == null) return null;

        product.setId(id);
        Product updated = productRepository.update(product);
        RedisUtil.delete(CACHE_KEY);
        return updated;
    }

    @Override
    public boolean deleteProduct(int id) {
        boolean deleted = productRepository.delete(id);
        if (deleted) RedisUtil.delete(CACHE_KEY); // invalidate cache
        return deleted;
    }
}
