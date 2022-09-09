package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeoSearchService {

    private final MongoTemplate mongoTemplate;
    private final GeoService geoService;


    public List<Offer> findMatchingOffer(GeoJsonPoint p) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "timeFrom"));
        query.addCriteria(Criteria.where("mpolygon").intersects(p));
        return mongoTemplate.find(query, Offer.class);
    }

    public List<Offer> searchWithinPolygon(String addressStr) {
        GeoJsonPoint point = geoService.getCoordinates(addressStr);
        return findMatchingOffer(point);
    }
}
