package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;

public record OfferSearchRequest(String accountId, Address address, String firstname) {
}
