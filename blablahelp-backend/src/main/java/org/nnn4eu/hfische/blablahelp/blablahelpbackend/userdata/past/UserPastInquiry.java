package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.past;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.MitshopperInquiry;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.Offer;
import org.springframework.data.annotation.Id;

import java.util.List;


public record UserPastInquiry(@Id String accountId, List<MitshopperInquiry> pastInquiries) {
}
