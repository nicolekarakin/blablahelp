package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInquiryResponse {
    private SearchOfferResponse offer;
    private MitshopperInquiryRecord inquiry;
}
