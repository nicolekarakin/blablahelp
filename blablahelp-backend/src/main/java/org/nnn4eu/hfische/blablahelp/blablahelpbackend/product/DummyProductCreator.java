package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev | local")
public class DummyProductCreator {
    private final ProductRepo productRepo;
    private final ShoppingListRepo shoppingListRepo;

    public void createProducts() {
        log.info("Runner in product db=====================================" + productRepo.count());
        if (productRepo.count() < 1L) {
            createAndSaveProducts();
        }
        log.info("Runner in product db=====================================" + productRepo.count());
    }

    public void addDummyShoppingList(String title, String accountId) {
        if (shoppingListRepo.findByTitleIgnoreCase(title).isEmpty()) {
            ShoppingList list = createDemoShoppingList(title, accountId);
            shoppingListRepo.save(list);
        }
    }

    private ShoppingList createDemoShoppingList(String title, String accountId) {
        List<@Valid ProductWrapper> products = new ArrayList<>();

        products.add(
                ProductWrapper.builder()
                        .amount(1f).unit(EUnit.L)
                        .title("Milch")
                        .category(productRepo.findByTitleIgnoreCase("Milch").get().getCategory())
                        .build());

        products.add(
                ProductWrapper.builder()
                        .amount(2.5f).unit(EUnit.KG)
                        .title("Kartoffel")
                        .category(productRepo.findByTitleIgnoreCase("Kartoffel").get().getCategory())
                        .build());
        products.add(
                ProductWrapper.builder()
                        .amount(1.5f).unit(EUnit.KG)
                        .title("Zwiebel")
                        .category(productRepo.findByTitleIgnoreCase("Zwiebel").get().getCategory())
                        .build());
        products.add(
                ProductWrapper.builder()
                        .amount(1f).unit(EUnit.PIECE)
                        .title("Paprika")
                        .category(productRepo.findByTitleIgnoreCase("Paprika").get().getCategory())
                        .build());
        products.add(
                ProductWrapper.builder()
                        .amount(3f).unit(EUnit.PIECE)
                        .title("Avocado")
                        .category(productRepo.findByTitleIgnoreCase("Avocado").get().getCategory())
                        .build());
        products.add(
                ProductWrapper.builder()
                        .amount(500f).unit(EUnit.G)
                        .note("Jakobs falls möglich und INTENSITÄT 3 aus 5")
                        .title("Kaffee")
                        .category(productRepo.findByTitleIgnoreCase("Kaffee").get().getCategory())
                        .build());
        products.add(
                ProductWrapper.builder()
                        .amount(500f).unit(EUnit.ML)
                        .title("Eiscreme")
                        .note("Schokolade, falls möglich eine von: Brands Zero, Ben&Jerry, Häagen-Dazs")
                        .category(productRepo.findByTitleIgnoreCase("Eiscreme").get().getCategory())
                        .build());

        return ShoppingList.builder()
                .products(products)
                .accountId(accountId)
                .title(title)
                .build();
    }

    private List<Product> createAndSaveProducts(Set<ECategory> categories, Set<String> titles) {
        List<Product> prs = new ArrayList<>();
        for (String i : titles) {
            Product a = Product.builder()
                    .title(i)
                    .status(EProductStatus.APPROVED)
                    .category(categories)
                    .build();
            prs.add(a);
        }
        return prs;
    }

    private void createAndSaveProducts() {
        Set<Product> allProducts = new HashSet<>();

        String[] fruits = {"Äpfel", "Bananen", "Trauben", "Orangen", "Erdbeeren", "Avocado", "Pfirsiche"};
        List<Product> prs1 = createAndSaveProducts(Set.of(ECategory.FRUITS), Set.of(fruits));
        allProducts.addAll(prs1);

        String[] vegetables = {"Blattsalate", "Brokkoli", "Paprika", "Tomaten", "Gurken", "Blumenkohl", "Zwiebel",
                "Wirsing", "Auberginen", "Kartoffel", "Karotte", "Knollensellerie", "Radieschen", "Knoblauch",
                "Frühlingszwiebel", "Spargel", "Bambussprossen", "Rosenkohl", "Erbsen", "Linsen", "Bohnen",
                "Zucchini", "Sellerie", "Lauch", "Pilze"};
        List<Product> prs2 = createAndSaveProducts(Set.of(ECategory.VEGETABLES), Set.of(vegetables));
        allProducts.addAll(prs2);

        String[] canned = {"Suppe", "Thunfisch", "Obst", "Bohnen", "Gemüse", "Soße"};
        List<Product> prs3 = createAndSaveProducts(Set.of(ECategory.CANNED), Set.of(canned));
        allProducts.addAll(prs3);

        String[] dairy = {"Milch", "Butter", "Käse", "Eier", "Joghurt", "Quark", "Sahne", "Rahm", "Frischkäse",
                "Saure Sahne", "Schmand", "Crème fraîche", "Ziegenkäse", "Schmelzkäse", "Camembert", "Mozzarella"};
        List<Product> prs4 = createAndSaveProducts(Set.of(ECategory.DAIRY), Set.of(dairy));
        allProducts.addAll(prs4);

        String[] meat = {"Huhn", "Rind", "Schwein", "Wurst", "Speck", "Salami", "Schinken", "Truthahn"};
        List<Product> prs5 = createAndSaveProducts(Set.of(ECategory.MEAT), Set.of(meat));
        allProducts.addAll(prs5);

        String[] sea = {"Meeresfrüchte", "Garnelen", "Krabben", "Kabeljau", "Thunfisch", "Lachs"};
        List<Product> prs6 = createAndSaveProducts(Set.of(ECategory.SEA), Set.of(sea));
        allProducts.addAll(prs6);

        String[] snacks = {"Chips", "Brezeln", "Popcorn", "Cracker", "Nüsse"};
        List<Product> prs7 = createAndSaveProducts(Set.of(ECategory.SNACK), Set.of(snacks));
        allProducts.addAll(prs7);

        String[] spices = {"Schwarzer Pfeffer", "Oregano", "Zimt", "Zucker", "Olivenöl", "Ketchup", "Mayonnaise"};
        List<Product> prs8 = createAndSaveProducts(Set.of(ECategory.SPICES), Set.of(spices));
        allProducts.addAll(prs8);

        String[] bread = {"Brot", "Tortillas", "Kuchen", "Muffins", "Bagels", "Kekse"};
        List<Product> prs9 = createAndSaveProducts(Set.of(ECategory.BREAD), Set.of(bread));
        allProducts.addAll(prs9);

        String[] drinks = {"Kaffee", "Teebeutel", "Milch", "Saft", "Limonade", "Bier", "Wein"};
        List<Product> prs10 = createAndSaveProducts(Set.of(ECategory.DRINK), Set.of(drinks));
        allProducts.addAll(prs10);

        String[] dryDrink = {"Kaffee", "Teebeutel", "Tee", "Kakao"};
        List<Product> prs11 = createAndSaveProducts(Set.of(ECategory.DRYDRINK), Set.of(dryDrink));
        allProducts.addAll(prs11);

        String[] baking = {"Mehl", "Puderzucker", "Backpulver"};
        List<Product> prs12 = createAndSaveProducts(Set.of(ECategory.BAKING), Set.of(baking));
        allProducts.addAll(prs12);

        String[] pasta = {"Haferflocken", "Müsli", "Braun Reis", "Weiß Reis", "Makkaroni", "Nudeln", "Spaghetti"};
        List<Product> prs13 = createAndSaveProducts(Set.of(ECategory.PASTARICE), Set.of(pasta));
        allProducts.addAll(prs13);

        String[] frozen = {"Pizza", "Fisch", "Gemüse", "Fertiggerichte", "Eiscreme"};
        List<Product> prs14 = createAndSaveProducts(Set.of(ECategory.FROZEN), Set.of(frozen));
        allProducts.addAll(prs14);

        String[] care = {"Shampoo", "Conditioner", "Deodorant", "Zahnpasta", "Zahnseide"};
        List<Product> prs15 = createAndSaveProducts(Set.of(ECategory.CARE), Set.of(care));
        allProducts.addAll(prs15);

        String[] haushold = {"Waschmittel", "Spülmittel", "Papierhandtücher", "Taschentücher", "Müllbeutel",
                "Aluminiumfolie", "Reißverschlussbeutel"};
        List<Product> prs16 = createAndSaveProducts(Set.of(ECategory.HOUSHOLD), Set.of(haushold));
        allProducts.addAll(prs16);

        String[] pet = {"Tiernahrung", "Katzenstreu", "Kauspielzeug", "Leckereien", "Tiershampoo"};
        List<Product> prs17 = createAndSaveProducts(Set.of(ECategory.PET), Set.of(pet));
        allProducts.addAll(prs17);

        productRepo.saveAll(allProducts);
    }
}
