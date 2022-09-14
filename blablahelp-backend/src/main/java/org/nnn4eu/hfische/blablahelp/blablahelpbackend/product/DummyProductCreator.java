package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.*;

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
        String str = "Kartoffel";
        Optional<Product> optional = productRepo.findByTitleIgnoreCase(str);
        if (optional.isPresent()) {
            products.add(
                    ProductWrapper.builder()
                            .amount(2.5f).unit(EUnit.KG)
                            .title(str)
                            .category(optional.get().getCategory())
                            .build());
        }
        str = "Zwiebel";
        optional = productRepo.findByTitleIgnoreCase(str);
        if (optional.isPresent()) {
            products.add(
                    ProductWrapper.builder()
                            .amount(2.5f).unit(EUnit.KG)
                            .title(str)
                            .category(optional.get().getCategory())
                            .build());
        }
        str = "Paprika";
        optional = productRepo.findByTitleIgnoreCase(str);
        if (optional.isPresent()) {
            products.add(
                    ProductWrapper.builder()
                            .amount(1f).unit(EUnit.PIECE)
                            .title(str)
                            .category(optional.get().getCategory())
                            .build());
        }
        str = "Avocado";
        optional = productRepo.findByTitleIgnoreCase(str);
        if (optional.isPresent()) {
            products.add(
                    ProductWrapper.builder()
                            .amount(3f).unit(EUnit.PIECE)
                            .title(str)
                            .category(optional.get().getCategory())
                            .build());
        }

        str = "Kaffee";
        optional = productRepo.findByTitleIgnoreCase(str);
        if (optional.isPresent()) {
            products.add(
                    ProductWrapper.builder()
                            .amount(500f).unit(EUnit.G)
                            .note("Jakobs falls möglich und INTENSITÄT 3 aus 5")
                            .title(str)
                            .category(optional.get().getCategory())
                            .build());
        }
        str = "Eiscreme";
        optional = productRepo.findByTitleIgnoreCase(str);
        if (optional.isPresent()) {
            products.add(
                    ProductWrapper.builder()
                            .amount(500f).unit(EUnit.ML)
                            .title(str)
                            .note("Schokolade, falls möglich eine von: Brands Zero, Ben&Jerry, Häagen-Dazs")
                            .category(optional.get().getCategory())
                            .build());
        }


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
            if (i.equals("Milch")) a.getCategory().add(ECategory.LIQUID);
            prs.add(a);
        }
        return prs;
    }

    private void createAndSaveProducts() {
        Set<Product> allProducts = new HashSet<>();

        String[] fruits = {"Äpfel", "Bananen", "Trauben", "Orangen", "Erdbeeren", "Avocado", "Pfirsiche"};
        List<Product> prs1 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.FRUITS)), Set.of(fruits));
        allProducts.addAll(prs1);

        String[] vegetables = {"Blattsalate", "Brokkoli", "Paprika", "Tomaten", "Gurken", "Blumenkohl", "Zwiebel",
                "Wirsing", "Auberginen", "Kartoffel", "Karotte", "Knollensellerie", "Radieschen", "Knoblauch",
                "Frühlingszwiebel", "Spargel", "Bambussprossen", "Rosenkohl", "Erbsen", "Linsen", "Bohnen",
                "Zucchini", "Sellerie", "Lauch", "Pilze"};
        List<Product> prs2 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.VEGETABLES)), Set.of(vegetables));
        allProducts.addAll(prs2);

        String[] canned = {"Suppe", "Thunfisch", "Obst", "Bohnen", "Gemüse", "Soße"};
        List<Product> prs3 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.CANNED)), Set.of(canned));
        allProducts.addAll(prs3);

        String[] dairy = {"Milch", "Butter", "Käse", "Eier", "Joghurt", "Quark", "Sahne", "Rahm", "Frischkäse",
                "Saure Sahne", "Schmand", "Crème fraîche", "Ziegenkäse", "Schmelzkäse", "Camembert", "Mozzarella"};
        List<Product> prs4 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.DAIRY)), Set.of(dairy));
        allProducts.addAll(prs4);

        String[] meat = {"Huhn", "Rind", "Schwein", "Wurst", "Speck", "Salami", "Schinken", "Truthahn"};
        List<Product> prs5 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.MEAT)), Set.of(meat));
        allProducts.addAll(prs5);

        String[] sea = {"Meeresfrüchte", "Garnelen", "Krabben", "Kabeljau", "Thunfisch", "Lachs"};
        List<Product> prs6 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.SEA)), Set.of(sea));
        allProducts.addAll(prs6);

        String[] snacks = {"Chips", "Brezeln", "Popcorn", "Cracker", "Nüsse"};
        List<Product> prs7 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.SNACK)), Set.of(snacks));
        allProducts.addAll(prs7);

        String[] spices = {"Schwarzer Pfeffer", "Oregano", "Zimt", "Zucker", "Olivenöl", "Ketchup", "Mayonnaise"};
        List<Product> prs8 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.SPICES)), Set.of(spices));
        allProducts.addAll(prs8);

        String[] bread = {"Brot", "Tortillas", "Kuchen", "Muffins", "Bagels", "Kekse"};
        List<Product> prs9 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.BREAD)), Set.of(bread));
        allProducts.addAll(prs9);

        String[] drinks = {"Kaffee", "Teebeutel", "Saft", "Limonade", "Bier", "Wein"};
        List<Product> prs10 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.DRINK)), Set.of(drinks));
        allProducts.addAll(prs10);

        String[] dryDrink = {"Kaffee", "Teebeutel", "Tee", "Kakao"};
        List<Product> prs11 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.DRYDRINK)), Set.of(dryDrink));
        allProducts.addAll(prs11);

        String[] baking = {"Mehl", "Puderzucker", "Backpulver"};
        List<Product> prs12 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.BAKING)), Set.of(baking));
        allProducts.addAll(prs12);

        String[] pasta = {"Haferflocken", "Müsli", "Braun Reis", "Weiß Reis", "Makkaroni", "Nudeln", "Spaghetti"};
        List<Product> prs13 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.PASTARICE)), Set.of(pasta));
        allProducts.addAll(prs13);

        String[] frozen = {"Pizza", "Fisch", "Gemüse", "Fertiggerichte", "Eiscreme"};
        List<Product> prs14 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.FROZEN)), Set.of(frozen));
        allProducts.addAll(prs14);

        String[] care = {"Shampoo", "Conditioner", "Deodorant", "Zahnpasta", "Zahnseide"};
        List<Product> prs15 = createAndSaveProducts(new HashSet<>(Arrays.asList(ECategory.CARE)), Set.of(care));
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
