package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoSearchService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfferRepoTest {
    @Autowired
    private OfferRepo offerRepo;
    @Autowired
    private MongoTemplate mongoTemplate;

    private GeoSearchService geoSearchService;
    private GeoService geoService;
    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6378.137;
    public int distanceKm = 2;
    public double distanceRadians = distanceKm / AVERAGE_RADIUS_OF_EARTH_KM;
    private Address addressSouth;
    private final Map<String, Offer> offersMap = new HashMap<>();
    @Mock
    ShopService shopService;
    @Mock
    UserDataRepo userDataRepo;
    @Mock
    WebClient webClient;

    @BeforeAll
    void init() throws JsonProcessingException {
        geoService = new GeoService(shopService);
        geoSearchService = new GeoSearchService(mongoTemplate, geoService, userDataRepo);

        log.info("offerCount: " + offerRepo.count());

        Account account = CreateData.createAccount();

        Offer offerLoc1 = CreateData.createOfferWithLoc(account.getId());
        offerLoc1.setMaxDistanceKm(distanceKm);
        GeoJsonPolygon area1 = geoService.calculatePolygon(offerLoc1.getDestinationAddress().getLoc(),
                offerLoc1.getShopAddress().getLoc(), offerLoc1.getMaxDistanceKm());
        offerLoc1.setMpolygon(area1);
        offerLoc1.setOfferId("offer1");
        offerLoc1 = offerRepo.save(offerLoc1);
        offersMap.put("offer1", offerLoc1);

        Offer offerLoc2 = CreateData.createOfferWithLoc(account.getId());
        offerLoc2.setShopAddress(CreateData.createAddressWithLocNoth());
        offerLoc2.setMaxDistanceKm(20);
        GeoJsonPolygon area2 = geoService.calculatePolygon(offerLoc2.getDestinationAddress().getLoc(),
                offerLoc2.getShopAddress().getLoc(), offerLoc2.getMaxDistanceKm());
        offerLoc2.setMpolygon(area2);
        offerLoc2.setOfferId("offer2");
        offerLoc2 = offerRepo.save(offerLoc2);
        offersMap.put("offer2", offerLoc2);

        Offer offerLoc3 = CreateData.createOfferWithLoc(account.getId());
        offerLoc3.setDestinationAddress(CreateData.createAddressWithLocNothEast());
        offerLoc3.setMaxDistanceKm(5);
        GeoJsonPolygon area3 = geoService.calculatePolygon(offerLoc3.getDestinationAddress().getLoc(),
                offerLoc3.getShopAddress().getLoc(), offerLoc3.getMaxDistanceKm());
        offerLoc3.setMpolygon(area3);
        offerLoc3.setOfferId("offer3");
        offerLoc3 = offerRepo.save(offerLoc3);
        offersMap.put("offer3", offerLoc3);

        log.info("offerCount: " + offerRepo.count());

        addressSouth = CreateData.createAddressWithLocSouth();

        log.info("\ndistance km: " + distanceKm + "\ndistance radian: " + distanceRadians);

        log.info("\n================================\n" +
                offerToStr(offerLoc1) + "\n" +
                offerToStr(offerLoc2) + "\n" +
                offerToStr(offerLoc3) + "\n" +
                "\nsouth address:\t" + addressSouth +
                "\n================================\n"
        );

    }


    private String offerToStr(Offer offer) {
        return new StringBuilder()
                .append("\noffer: ")
                .append(offer.getOfferId())
                .append(", distKm: ")
                .append(offer.getMaxDistanceKm())
                .append("\nshop: ")
                .append(offer.getShopAddress())
                .append("\ndestination: ")
                .append(offer.getDestinationAddress())
                .toString();
    }

    @Test
    @DisplayName("searchOffersPolygon")
    void searchOffersPolygon() throws JsonProcessingException {
        List<String> ids1 = searchOffersPolygon(new GeoJsonPoint(11.49813, 48.17574));
        assertThat("", ids1, hasSize(2));
        assertThat("", ids1, hasItem("offer2"));
        assertThat("", ids1, hasItem("offer3"));

        List<String> ids2 = searchOffersPolygon(new GeoJsonPoint(11.43692, 48.22446));
        assertThat("", ids2, hasSize(0));

        List<String> ids3 = searchOffersPolygon(new GeoJsonPoint(11.52312, 48.2026));
        assertThat("", ids3, hasSize(1));
        assertThat("", ids3.get(0), equalTo(offersMap.get("offer2").getOfferId()));

        List<String> ids4 = searchOffersPolygon(new GeoJsonPoint(11.58121, 48.26131));
        assertThat("", ids4, hasSize(0));
    }

    List<String> searchOffersPolygon(GeoJsonPoint p) {
        List<Offer> offers = geoSearchService.findMatchingNotExpiredOffers(p);
        List<String> ids = offers.stream().map(a -> a.getOfferId()).toList();
        log.info("\n================================\n" + p.getX() + " " + p.getY() +
                "\nids: " + Arrays.toString(ids.toArray()));
        return ids;
    }

    @Test
    @DisplayName("findByShopAddress_LocWithinCustom")
    void findByShopAddress_LocWithinCustom() throws JsonProcessingException {

        Circle circle = new Circle(addressSouth.getLoc(), new Distance(distanceKm, Metrics.KILOMETERS));

        List<Offer> offers2 = offerRepo.findByShopAddrWithinCircle(circle.getCenter().getX(),
                circle.getCenter().getY(), circle.getRadius().getNormalizedValue());
        Assertions.assertEquals(2, offers2.size());
        Assertions.assertEquals(offersMap.get("offer1").getOfferId(), offers2.get(0).getOfferId());

        List<Offer> offers3 = offerRepo.findByShopAddrWithinCircle(addressSouth.getLoc().getX(),
                addressSouth.getLoc().getY(), distanceRadians);
        Assertions.assertEquals(2, offers3.size());
        Assertions.assertEquals(offersMap.get("offer1").getOfferId(), offers3.get(0).getOfferId());

    }

}

/*
mongosh
https://www.mongodb.com/docs/manual/tutorial/calculate-distances-using-spherical-geometry-with-2d-geospatial-indexes/


//works!!!!! many results will be back!!
db.shopList.aggregate(
  // Start with a $match pipeline which can take advantage of an index and limit documents processed
  { $match : {
     "addresses.city": "München"
  }},
  { $unwind : "$addresses" },
  { $match : {
     "addresses.city": "München"
  }}
)

//works, 1 result
db.shopList.find({}, {addresses: {$elemMatch: {city: "Augsburg"}}});
db.shopList.find({}, {addresses: {$elemMatch: {city: "Berlin"}}});
db.shopList.find({}, {addresses: {$elemMatch: {city: "München"}}});

//no error and no output
db.getCollection('addresses').find({
    $and: [{
        addresses: {$elemMatch: {city:{$in: ["Augsburg", "München"]}}}
    },
    {
        addresses: {$not: {$elemMatch: {zip:{$nin: ["81370", "81371"]}} }}
    }]})

//returns 1 result
db.shopList.find( { },
 { addresses :
     { $elemMatch :
            { loc : {$geoWithin: {$centerSphere: [ [  11.5372926, 48.125021 ], 100/6371 ]}} }
     }
 })

//all 3 work!!!
db.offer.find({ "shopAddress.city" : "München" }).pretty();
db.offer.find({"shopAddress.city": "Augsburg"});
db.offer.find({
 "shopAddress.loc" : { $geoWithin: { $centerSphere: [ [  11.5372926, 48.125021 ], 100/6371 ] } }
})

db.offer.find({"isExpired":false})

//works, only one result for Mühlfeldweg 46, 85748 Garching bei München
db.offer.find({
             "destinationAddress.loc" : { $geoWithin: { $centerSphere: [ [  11.655268759092749, 48.24623592757452 ], 2/6371 ] } }
}).pretty()

db.offer.find({
 "mpolygon" :{  $geoIntersects:{  $geometry: {type : "Point", "coordinates": [11.546708857432789,48.139356983477]}  }}
})

db.offer.find({
 "mpolygon" :{  $geoIntersects:{  $geometry: {type : "Point", "coordinates": [11.545334,48.203102]}  }}
})

db.offer.aggregate([
  { $project : { _id : 0, destinationAddress : 1, shopname : 1, priceOffer : 1 } }
]).pretty()

db.offer.aggregate([
  { $group : { _id : '$shopAddress.zip', totaldocs : { $sum : 1 } } }
]).pretty()

db.offer.aggregate([
  { $group : { _id : '$accountId', totaldocs : { $sum : 1 } } }
]).pretty()

//assumes there is only one index <<< db.offer.createIndex({"shopAddress.loc":"2dsphere"})
db.offer.find({"shopAddress.loc":{ $nearSphere:
          {
            $geometry: { type: "Point",  coordinates: [11.546708857432789,48.139356983477] },
            $minDistance: 1000,
            $maxDistance: 5000
          }
}})

db.offer.aggregate([
    {$geoNear:{
        near:
            {type:"Point", coordinates:[11.546708857432789,48.139356983477]},
            distanceField: "shopDistance",
            $minDistance: 1000,
            $maxDistance: 5000,
            spherical: true
     }}
]).pretty()

db.help
db.user.getIndexes
db.shopList.getIndexes()
db.shopList.createIndex({"addresses.loc":"2dsphere"})
db.offer.createIndex({"shopAddress.loc":"2dsphere"})
db.offer.createIndex({"destinationAddress.loc":"2dsphere"})
//only if all polygons are closed and have correct order of points
db.offer.createIndex({"mpolygon": "2dsphere" });

----not

db.offer.ensureIndex({"mpolygon": "2dsphere" });
lat: 0.000707 long:0.001267 >>> 4 metres or roughly .000036036 >>> 111km or 1degree
.004km/111km >> 0.00003603603 degree


{
Query query = new Query();
query.with(Sort.by(Sort.Direction.DESC, "timeFrom"));
query.addCriteria(
     new Criteria().andOperator(
                        Criteria.where("accountId").ne(accountId),
                        Criteria.where("mpolygon").intersects(p),
                        Criteria.where("isExpired").is(false)
     )
);
return mongoTemplate.find(query, Offer.class);
}

 */