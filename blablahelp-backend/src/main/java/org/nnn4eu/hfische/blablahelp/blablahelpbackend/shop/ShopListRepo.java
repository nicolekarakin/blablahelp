package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.Set;

public interface ShopListRepo extends MongoRepository<ShopList, String> {

    @Query(value = "{ 'id' : { '$regex' : ?0 , $options: 'i'}}", fields = "{ 'id' : 1 }")
    Set<ShopList> findByIdIgnoreCaseContaining(String idpart);

    @Query(value = "{'$and':[ {'id': ?0}, { 'addresses.city' : { '$regex' : ?1 , $options: 'i'}} ] }",
           fields = "{ 'addresses.city' : 1,'addresses.zip' : 1,'addresses.street' : 1 }")
    Collection<ShopList> findByIdAndAddressFilter(String id, String search);

}
