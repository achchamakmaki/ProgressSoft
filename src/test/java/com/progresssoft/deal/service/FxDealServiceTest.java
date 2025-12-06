package com.progresssoft.deal.service;

import com.progresssoft.deal.dto.FxDealRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FxDealServiceTest {

    @Autowired
    private FxDealService service;

    @Test
    void shouldImportValidDeal() {
        var request = new FxDealRequest("TEST-001", "USD", "EUR", Instant.now(), BigDecimal.valueOf(1000.50));

        var summary = service.importDeals(List.of(request));

        assertEquals(1, summary.successfullyImported());
        assertTrue(summary.errors().isEmpty());
    }

    @Test
    void shouldRejectDuplicateDeal() {
        var request = new FxDealRequest("DUP-001", "GBP", "JPY", Instant.now(), BigDecimal.valueOf(5000));

        service.importDeals(List.of(request));
        var summary = service.importDeals(List.of(request));

        assertEquals(0, summary.successfullyImported());
        assertFalse(summary.errors().isEmpty());
        assertTrue(summary.errors().get(0).contains("Duplicate"));
    }

    @Test
    void shouldRejectInvalidCurrency() {
        var request = new FxDealRequest("BAD-001", "XYZ1", "EUR", Instant.now(), BigDecimal.valueOf(100));

        var summary = service.importDeals(List.of(request));

        assertEquals(0, summary.successfullyImported());
        assertEquals(1, summary.errors().size());

        // Message exact généré par le service après correction
        String errorMessage = summary.errors().get(0);
        assertTrue(errorMessage.contains("Invalid fromCurrency") ||
                errorMessage.contains("must be exactly 3 uppercase letters"));
    }
}
