package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model.ForwardResponse;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model.Loc;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeoService {
    @Value("${pstoken:nourlfortests}")
    private String token;
    private static final String DEFAULT_COUNTRY = "DE";
    private final ShopService shopService;
    private final WebClient webClient = WebClient.create("http://api.positionstack.com");
//    https://github.com/reactor/reactor-netty/issues/1774
//    https://stackoverflow.com/questions/67316160/what-is-wrong-with-below-webclient-config

    public void addCoordinatesToShops(String shopListId) {
        String alpha3Country = getIso3CountryFromShopListId(shopListId);
        log.info("shopListId: " + shopListId);
        Set<Address> addresses = shopService.findAddressesByListId(shopListId);
        addresses.forEach(a -> {

            if (a.getCountry() != null && a.getCountry().isEmpty()) a.setCountry(alpha3Country);
            if (a.getLoc() == null) {
                GeoJsonPoint point = getCoordinatesForAddress(a);
                a.setLoc(point);
            }
        });
        shopService.addAddresses(shopListId, addresses);
    }

    public GeoJsonPoint getCoordinatesForAddress(Address address) {
        String country = (address.getCountry() == null) ?
                new Locale("de", DEFAULT_COUNTRY).getISO3Country()
                : address.getCountry();
        String addressStr = address.getStreet() + " " + address.getZip() + " " + address.getCity() + " " + country;

        return getCoordinates(addressStr);
    }

    public GeoJsonPoint getCoordinates(String addressStr) {
        String query = addressStr.replaceAll("[:.,;@$&\\\\|/]", " ");
        log.info("token ok " + (!token.isEmpty()) + ", Looking for address: " + addressStr);
        ForwardResponse response = webClient.get()
                .uri(builder -> builder.path("/v1/forward")
                        .queryParam("access_key", token)
                        .queryParam("query", query)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ForwardResponse.class)
                .retry(3)
                .log()
                .block();
        if (response != null) {
            List<Loc> locs = response.data();//TODO(@nicolekarakin) should we use confidence levels?
            log.info("Address to Geolocation: " + Arrays.toString(locs.toArray()));
            return new GeoJsonPoint(locs.get(0).longitude(), locs.get(0).latitude());
        } else throw new IllegalArgumentException("Couldn't fetch geocoordinates for provided address");
    }

    private String getIso3CountryFromShopListId(String shopListId) {
        String iso2;
        String[] infoStr = shopListId.split("-");
        if (infoStr.length != 2) throw new IllegalArgumentException("ShopListId is in a wrong format");
        else iso2 = infoStr[1].split("_")[0];
        return new Locale("de", iso2).getISO3Country();
    }

    public GeoJsonPolygon calculatePolygon(GeoJsonPoint loc1, GeoJsonPoint loc2, double maxDistanceKm) {
        Point a, b;
        if (loc2.getY() >= loc1.getY()) {
            a = new Point(loc1.getX(), loc1.getY());
            b = new Point(loc2.getX(), loc2.getY());
        } else {
            b = new Point(loc1.getX(), loc1.getY());
            a = new Point(loc2.getX(), loc2.getY());
        }

        double m = (a.getX() - b.getX()) / (b.getY() - a.getY());
        double maxDistanceDegree = maxDistanceKm / 111.111;

        double coef = maxDistanceDegree / 2;
        double v = coef / Math.sqrt(1 + (m * m));
        double sx, rx, tx, ux, sy, ry, ty, uy;
        if (m <= 0) {
            sx = b.getX() - v;
            rx = b.getX() + v;
            tx = a.getX() + v;
            ux = a.getX() - v;
        } else {
            sx = b.getX() + v;
            rx = b.getX() - v;
            tx = a.getX() - v;
            ux = a.getX() + v;
        }
        ry = m * (rx - b.getX()) + b.getY();
        sy = m * (sx - b.getX()) + b.getY();
        ty = m * (tx - a.getX()) + a.getY();
        uy = m * (ux - a.getX()) + a.getY();

        List<GeoJsonPoint> geoJsonPoints = new ArrayList<>();

        geoJsonPoints.add(new GeoJsonPoint(rx, ry));
        geoJsonPoints.add(new GeoJsonPoint(sx, sy));

        geoJsonPoints.add(new GeoJsonPoint(ux, uy));
        geoJsonPoints.add(new GeoJsonPoint(tx, ty));

        geoJsonPoints.add(new GeoJsonPoint(rx, ry));
        List<Point> points = geoJsonPoints.stream()
                .map(ap -> new Point(ap.getX(), ap.getY())).toList();

        return new GeoJsonPolygon(points);
    }


}
