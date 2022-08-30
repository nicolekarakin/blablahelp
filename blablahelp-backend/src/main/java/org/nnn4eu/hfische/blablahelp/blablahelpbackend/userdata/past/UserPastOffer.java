package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.past;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.Offer;
import org.springframework.data.annotation.Id;

import java.util.List;


public record UserPastOffer(@Id String accountId, List<Offer> pastOffers) {
}
