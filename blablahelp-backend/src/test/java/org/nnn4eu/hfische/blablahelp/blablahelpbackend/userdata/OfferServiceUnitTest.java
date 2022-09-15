package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfferServiceUnitTest {
    WebClient webClient = mock(WebClient.class);
    private final ShopService shopService = mock(ShopService.class);
    private final GeoService geoService = new GeoService(shopService);
    final static double RADIUS_MAJOR = 6378137.0;
    final static double RADIUS_MINOR = 6356752.3142;
    public int distanceKm1 = 12;
    public int distanceKm2 = 5;
    private final Map<String, Offer> offersMap = new HashMap<>();

    @BeforeAll
    void init() throws JsonProcessingException {
        Account account = CreateData.createAccount();

        Offer offerLoc1 = CreateData.createOfferWithLoc(account.getId());
        offerLoc1.setMaxDistanceKm(distanceKm1);
        offersMap.put("80339", offerLoc1);

        Offer offerLoc2 = CreateData.createOfferWithLoc(account.getId());
        offerLoc2.setDestinationAddress(CreateData.createAddressWithLocNoth());
        offerLoc2.setMaxDistanceKm(distanceKm2);
        offersMap.put("80797", offerLoc2);

        Offer offerLoc3 = CreateData.createOfferWithLoc(account.getId());
        offerLoc3.setDestinationAddress(CreateData.createAddressWithLocNothEast());
        offerLoc3.setMaxDistanceKm(distanceKm1);
        offersMap.put("80999", offerLoc3);


    }

    @Test
    void createBoundingBoxTests() throws JsonProcessingException {
        createBoundingBox(offersMap.get("80339"));
        createBoundingBox(offersMap.get("80797"));
        createBoundingBox(offersMap.get("80999"));
    }


    void createBoundingBox(Offer offer) throws JsonProcessingException {

        GeoJsonPolygon polygon = geoService.calculatePolygon(offer.getShopAddress().getLoc(),
                offer.getDestinationAddress().getLoc(),
                offer.getMaxDistanceKm());
        log.info("========================input km " + offer.getMaxDistanceKm() + " coordinates:\n" + offer.getShopAddress().getLoc() +
                "\n" + offer.getDestinationAddress().getLoc());
        List<Point> points = polygon.getCoordinates().stream()
                .map(a -> a.getCoordinates())
                .flatMap(List::stream)
                .toList();
        String pointsStr = points.stream()
                .reduce("", (subtotal, element) ->
                        subtotal + "\nx: " + element.getX() + " y: " + element.getY(), String::concat
                );

        log.info("\n========================" + pointsStr);
        Assertions.assertEquals(5, points.size());

        double kmLength = getKmBtwPoints(offer.getDestinationAddress().getLoc(), offer.getShopAddress().getLoc());
        double kmWidth1 = getKmBtwPoints(points.get(0), points.get(1));
        double kmWidth2 = getKmBtwPoints(points.get(2), points.get(3));
        double kmLength1 = getKmBtwPoints(points.get(0), points.get(2));
        double kmLength2 = getKmBtwPoints(points.get(1), points.get(3));
        log.info("\n========================\nkmLength: " + kmLength +
                "\nkmLength1: " + kmLength1 +
                "\nkmLength2: " + kmLength2 +
                "\nkmWidth: " + offer.getMaxDistanceKm() +
                "\nkmWidth1: " + kmWidth1 +
                "\nkmWidth2: " + kmWidth2
        );
        Assertions.assertEquals(Math.floor(kmLength1), Math.floor(kmLength2));
        Assertions.assertEquals(Math.floor(kmWidth1), Math.floor(kmWidth2));
    }

    public double getKmBtwPoints(Point p1, Point p2) {
        return calculateDistanceInKilometer(p1.getY(), p1.getX(),
                p2.getY(), p2.getX());
    }

    public double calculateDistanceInKilometer(double userLat, double userLng,
                                               double venueLat, double venueLng) {

        double x1 = xAxisProjection(userLat);
        double x2 = xAxisProjection(venueLat);

        double y1 = yAxisProjection(userLng);
        double y2 = yAxisProjection(venueLng);
        double summ = Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2);
        return Math.sqrt(summ) / 1000;
    }

    double xAxisProjection(double input) {
        return RADIUS_MAJOR * Math.toRadians(input);
    }

    double yAxisProjection(double input) {
        input = Math.min(Math.max(input, -89.5), 89.5);
        double earthDimensionalRateNormalized = 1.0 - Math.pow(RADIUS_MINOR / RADIUS_MAJOR, 2);

        double inputOnEarthProj = Math.sqrt(earthDimensionalRateNormalized) *
                Math.sin(Math.toRadians(input));

        inputOnEarthProj = Math.pow(((1.0 - inputOnEarthProj) / (1.0 + inputOnEarthProj)),
                0.5 * Math.sqrt(earthDimensionalRateNormalized));

        double inputOnEarthProjNormalized =
                Math.tan(0.5 * ((Math.PI * 0.5) - Math.toRadians(input))) / inputOnEarthProj;

        return (-1) * RADIUS_MAJOR * Math.log(inputOnEarthProjNormalized);
    }

    private double calculateDistanceInKilometerHaversine(double userLat, double userLng,
                                                         double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double AVERAGE_RADIUS_OF_EARTH_KM = 6378.137;
        return (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }

    public double getKmBtwPoints(GeoJsonPoint p1, GeoJsonPoint p2) {
        return calculateDistanceInKilometerHaversine(p1.getY(), p1.getX(),
                p2.getY(), p2.getX());
    }
}


