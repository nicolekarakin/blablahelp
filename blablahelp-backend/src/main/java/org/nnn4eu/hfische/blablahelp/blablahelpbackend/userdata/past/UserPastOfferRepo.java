package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.past;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPastOfferRepo extends MongoRepository<UserPastOffer, String> {

}
