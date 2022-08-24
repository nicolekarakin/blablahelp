package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface ShopListRepo extends MongoRepository<ShopList, String> {

    //  @Query("{'$and':[ {'id': ?0}, {'addresses$city': ?1} ] }")
    Collection<Address> findByIdAndAddresses_StreetOrAddresses_ZipOrAddresses_CityIgnoreCaseContaining(String id, String search);

    Collection<Address> findByIdAndAddresses_CityAndAddresses_StreetOrAddresses_ZipIgnoreCaseContaining(String id, String city, String search);
}
