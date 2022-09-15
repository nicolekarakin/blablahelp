package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
class DummyProductCreatorTest {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ShoppingListRepo shoppingListRepo;

    @Test
    @DirtiesContext
    void createAndSaveProducts() {
        DummyProductCreator creator = new DummyProductCreator(productRepo, shoppingListRepo);
        Long count1 = productRepo.count();
        creator.createProducts();
        Long count2 = productRepo.count();
        Assertions.assertEquals(0, count1);
        Assertions.assertEquals(123, count2);
    }

    @Test
    @DirtiesContext
    void createProducts() {
        DummyProductCreator creator = new DummyProductCreator(productRepo, shoppingListRepo);
        Long count1 = shoppingListRepo.count();
        String id = UUID.randomUUID().toString();
        creator.addDummyShoppingList("String title", id);
        Long count2 = shoppingListRepo.count();
        Assertions.assertEquals(0, count1);
        Assertions.assertEquals(1, count2);
    }
}