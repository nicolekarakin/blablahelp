package org.nnn4eu.hfische.blablahelp.blablahelpbackend;

import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BlablahelpBackendApplicationTests {

    @MockBean
    GeoService geoService;

    @Test
    void contextLoads() {
    }


}
