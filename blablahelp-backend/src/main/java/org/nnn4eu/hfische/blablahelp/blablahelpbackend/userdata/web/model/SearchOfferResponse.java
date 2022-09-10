package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Review;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;

import java.math.BigDecimal;
import java.util.List;

public record SearchOfferResponse(
        String offerId,
        Long timeFrom,
        Long timeTo,
        Long shoppingDay,
        String shopname,
        Address shopAddress,
        BigDecimal priceOffer,
        String notes,
        int maxMitshoppers, int maxLiter, int maxArticles, int maxDistanceKm,
        String firstname,
        String motto,
        int shoppingCancellation,
        int shoppingCount,
        int shoppingRating,
        List<Review> reviewsForShopping
) {
    public static SearchOfferResponse from(Offer o, UserData u) {
        return new SearchOfferResponse(
                o.getOfferId(),
                o.getTimeFrom(),
                o.getTimeTo(),
                o.getShoppingDay(),
                o.getShopname(),
                o.getShopAddress(),
                o.getPriceOffer(),
                o.getNotes(),
                o.getMaxMitshoppers(),
                o.getMaxLiter(),
                o.getMaxArticles(),
                o.getMaxDistanceKm(),
                u.getFirstname(),
                u.getMotto(),
                u.getShoppingCancellation(),
                u.getShoppingCount(),
                u.getShoppingRating(),
                u.getReviewsForShopping()
        );
    }

}


