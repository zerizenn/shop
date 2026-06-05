package by.vstu.shop.service;

import by.vstu.shop.dto.ProductDTO;
import by.vstu.shop.entity.Category;
import by.vstu.shop.entity.Product;
import by.vstu.shop.repository.CategoryRepository;
import by.vstu.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    //эндпоинт: все товары по категории
    public List<Product> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    //эндпоинт: товары не дороже maxPrice
    public List<Product> getByMaxPrice(BigDecimal maxPrice) {
        return productRepository.findByPriceLessThanEqual(maxPrice);
    }

    //эндпоинт: только товары в наличии
    public List<Product> getInStock() {
        return productRepository.findAllInStock();
    }

    public Product create(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    public Product update(Long id, ProductDTO dto) {
        Product product = getById(id);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = getById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }
}
