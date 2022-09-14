package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DummyProductCreatorTest {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ShoppingListRepo shoppingListRepo;

    private DummyProductCreator creator;

    @BeforeAll
    void init() throws JsonProcessingException {
        creator = new DummyProductCreator(productRepo, shoppingListRepo);
    }

    @Test
    void createAndSaveProducts() {
        Long count1 = productRepo.count();
        creator.createProducts();
        Long count2 = productRepo.count();
        Assertions.assertEquals(0, count1);
        Assertions.assertEquals(123, count2);
    }
}