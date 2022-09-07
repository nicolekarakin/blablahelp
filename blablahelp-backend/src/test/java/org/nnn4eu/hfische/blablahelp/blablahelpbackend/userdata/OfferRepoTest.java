package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

//@ComponentScan(basePackageClasses = HttpMessageConvertersConfig.class)
@Slf4j
@DataMongoTest
class OfferRepoTest {

    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6378.137;//actually 6371??

    @Autowired
    private OfferRepo offerRepo;//MongoTemplate mongoTemplate;

    @Test
    void findByShopAddress_LocWithin() throws JsonProcessingException {
        Account account = CreateData.createAccount();
        Offer offerLoc1 = CreateData.createOfferWithLoc(account.getId());
        offerLoc1.setMaxDistanceKm(20);
        offerLoc1 = offerRepo.save(offerLoc1);

        Offer offerLoc2 = CreateData.createOfferWithLoc(account.getId());
        offerLoc2.setShopAddress(CreateData.createAddressWithLocNoth());
        offerLoc2.setDestinationAddress(CreateData.createAddressWithLocNoth());
        offerLoc2 = offerRepo.save(offerLoc2);

        Address addressSouth = CreateData.createAddressWithLocSouth();

        double testDistance = 9.7;
        Circle reverseCircle = new Circle(addressSouth.getLoc(), new Distance(testDistance, Metrics.KILOMETERS));
        List<Offer> offers = offerRepo.findByShopAddress_LocWithin(reverseCircle);

        log.info("\ntest Distance: " + testDistance);
        int kmToShop1 = getKmBtwPoints(addressSouth.getLoc(), offerLoc1.getShopAddress().getLoc());
        int kmToShop2 = getKmBtwPoints(addressSouth.getLoc(), offerLoc2.getShopAddress().getLoc());


        log.info("\n================================\n" +
                "\naddr1:\t" + offerLoc1.getShopAddress() +
                "\naddr2:\t" + offerLoc2.getShopAddress() +
                "\nsouth address:\t" + addressSouth +
                "\nkm Distance btw shopOffer1 and south address: " + kmToShop1 +
                "\nkm Distance btw shopOffer2 and south address: " + kmToShop2 +

                "\n================================\n"
        );

//        addr1:	Address{ Theresienhöhe 5, 80339 München null, loc[11.546724, 48.136749, Point }
//        addr2:	Address{ Schleißheimer Str. 466/468, 80797 München null, loc[11.581812, 48.210053, Point }
//        south address:	Address{ Martin-Behaim-Straße 3, 81373 München null, loc[11.5372926, 48.125021, Point }

//        km Distance btw shopOffer1 and south address: 1 << google 1.55km
//        km Distance btw shopOffer2 and south address: 10 << google 10.6km

        Assertions.assertEquals(1, offers.size());
        Assertions.assertEquals(offers.get(0).getOfferId(), offerLoc1.getOfferId());
    }

    public int getKmBtwPoints(GeoJsonPoint p1, GeoJsonPoint p2) {
        return calculateDistanceInKilometerHaversine(p1.getY(), p1.getX(),
                p2.getY(), p2.getX());
    }

    //    https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula/27943#27943
    public int calculateDistanceInKilometerHaversine(double userLat, double userLng,
                                                     double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
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

//works, only one result for Mühlfeldweg 46, 85748 Garching bei München
db.offer.find({
             "destinationAddress.loc" : { $geoWithin: { $centerSphere: [ [  11.655268759092749, 48.24623592757452 ], 2/6371 ] } }
}).pretty()

 */