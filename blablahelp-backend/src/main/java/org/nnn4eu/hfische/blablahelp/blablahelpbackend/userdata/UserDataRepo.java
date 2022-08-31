package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepo extends MongoRepository<UserData, String> {

}
