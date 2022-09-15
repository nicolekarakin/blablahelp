package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepo extends MongoRepository<Product, String> {
    Optional<Product> findByTitleIgnoreCase(String title);
}
