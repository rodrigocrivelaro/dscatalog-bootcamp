package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseIntegrityException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged(PageRequest pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(x -> new ProductDto(x, x.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        Product product = productOpt.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado!"));
        return new ProductDto(product, product.getCategories());
    }

    @Transactional
    public ProductDto insert(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product = productRepository.save(product);
        return new ProductDto(product);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto productDto) {
        try {
            Product product = productRepository.getReferenceById(id);
            product.setName(productDto.getName());
            product = productRepository.save(product);
            return new ProductDto(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("O id: " + id + " não foi encontrado.");
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("O id: " + id + " não foi encontrado.");
        } catch (DataIntegrityViolationException d) {
            throw new DatabaseIntegrityException("O produto " + id + " esta em uso e não pode ser excluído.");
        }
    }
}
