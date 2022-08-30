package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.web.ctrl;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.web.model.ShopNameList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.BASIC)
public class ShopCtrl {
    private final ShopService shopService;
    @GetMapping(path = "/{country}/shopnames")
    public ResponseEntity<ShopNameList> getShopnamesByCountry(@NotBlank  @PathVariable String country) {
        return  new ResponseEntity<>(new ShopNameList(shopService.findAllShopIdsForCountry(country)), HttpStatus.OK);
    }

    @GetMapping(path = "/{locale}/shops/{shopname}/addresses")
    public ResponseEntity<Set<Address>> getShopAddressesByCountry(@NotBlank @PathVariable String locale,
                                                                  @NotBlank @PathVariable String shopname,
                                                                  @NotBlank @RequestParam String city,
                                                                  @RequestParam Optional<String> search) {
        String id = locale +"_"+shopname.toLowerCase();
        String searchValue = search.orElse("");
        return  new ResponseEntity<>(shopService.findAddressesByListIdCitySearch(id,city,searchValue), HttpStatus.OK);
    }

}
