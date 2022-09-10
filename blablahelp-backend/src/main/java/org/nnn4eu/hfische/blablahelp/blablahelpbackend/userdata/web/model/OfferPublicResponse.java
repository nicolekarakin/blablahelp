package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model;

import java.time.Instant;

public record OfferPublicResponse(String firstname,
                                  String shopCity,
                                  String shopname,
                                  Instant shoppingDay,
                                  String motto,
                                  int shoppingCancellation,
                                  int shoppingCount,
                                  int shoppingRating
) {//TODO should we include reviews?
}
