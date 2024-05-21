package ru.themlyakov.storeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.themlyakov.storeproject.entity.Product;
import ru.themlyakov.storeproject.repository.ProductRepository;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product insert(Product product) {
        return productRepository.insert(product);
    }

    public Product update(Product product) {
        if (product.getId() == null) {
            throw new DataManipulationException("Id is null");
        }
        return productRepository.update(product);
    }

    public Product delete(Long id) {
        return productRepository.delete(id);
    }

    public List<Product> selectAll() {
        return productRepository.selectAll();
    }

    public Product selectById(Long id) {
        return productRepository.selectById(id);
    }
}
