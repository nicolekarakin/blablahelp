package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ShopService {
    private final ShopListRepo shopListRepo;

    public Set<String> findAllShopIdsForCountry(String country){
        return shopListRepo.findByIdIgnoreCaseContaining("-"+country.toUpperCase()).stream()
                .map(a->a.getId().split("_")[1].toUpperCase())
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Address> findAddressesByListId(@NotBlank String id) {
        Optional<ShopList> value = shopListRepo.findById(id);
        if (value.isPresent() && !value.get().getAddresses().isEmpty()) {
            return value.get().getAddresses();
        }
        return Collections.emptySet();
    }

    public Set<Address> findAddressesByListIdCitySearch(@NotBlank String id, @NotBlank String city, String search) {
        Set<Address> addresses = findAddressesByListId(id);
        Stream<Address> filtered=addresses.stream()
                .filter(a -> city.equalsIgnoreCase(a.city()));
        if(search!=null || !search.trim().isEmpty()) {
            return filtered.filter(a -> (a.street().toLowerCase().contains(search.toLowerCase()) ||
                            a.zip().toLowerCase().contains(search.toLowerCase())))
                    .collect(Collectors.toSet());
        }
        else
            return filtered.collect(Collectors.toSet());
    }

    public ShopList addAddresses(@NotBlank String id, @NotEmpty Set<Address> addresses) {
        ShopList shopList;
        Optional<ShopList> value = shopListRepo.findById(id);
        if (value.isPresent()) {
            shopList = value.get();
            shopList.getAddresses().addAll(addresses);
        } else {
            shopList = new ShopList(id, addresses);
        }
        return shopListRepo.save(shopList);
    }
}
