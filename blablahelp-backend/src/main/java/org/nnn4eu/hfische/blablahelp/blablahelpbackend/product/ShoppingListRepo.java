package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.ShoppingList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShoppingListRepo extends MongoRepository<ShoppingList, String> {
    Optional<ShoppingList> findByTitleIgnoreCase(String title);
}
