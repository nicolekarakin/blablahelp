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

@RequiredArgsConstructor
@Service
public class ShopService {
    private final ShopListRepo shopListRepo;

    public Set<Address> findAddressesByListId(@NotBlank String id) {
        Optional<ShopList> value = shopListRepo.findById(id);
        if (value.isPresent() && !value.get().getAddresses().isEmpty()) {
            return value.get().getAddresses();
        }
        return Collections.emptySet();
    }

    public Set<Address> findAddressesByListIdCitySearch(@NotBlank String id, @NotBlank String city, @NotBlank String search) {
        Set<Address> addresses = findAddressesByListId(id);
        return addresses.stream()
                .filter(a -> city.equals(a.city()) && (search.contains(a.street()) || search.contains(a.zip())))
                .collect(Collectors.toSet());
    }

    public ShopList addAddresses(@NotBlank String id, @NotEmpty Set<Address> addresses) {
        ShopList shopList = null;
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
