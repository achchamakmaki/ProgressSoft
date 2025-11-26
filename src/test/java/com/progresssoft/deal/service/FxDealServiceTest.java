package com.progresssoft.deal.service;

import com.progresssoft.deal.dto.FxDealRequest;
import com.progresssoft.deal.repository.FxDealRepository;
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

    @Autowired
    private FxDealRepository repository;

    @Test
    void shouldImportValidDeal() {
        repository.deleteAll();

        var request = new FxDealRequest("TEST-001", "USD", "EUR", Instant.now(), BigDecimal.valueOf(1000.50));

        var summary = service.importDeals(List.of(request));

        assertEquals(1, summary.successfullyImported());
        assertTrue(summary.errors().isEmpty());
        assertTrue(repository.existsByDealUniqueId("TEST-001"));
    }

    @Test
    void shouldRejectDuplicateDeal() {
        repository.deleteAll();
        var request = new FxDealRequest("DUP-001", "GBP", "JPY", Instant.now(), BigDecimal.valueOf(5000));

        service.importDeals(List.of(request)); // premier → OK
        var summary = service.importDeals(List.of(request)); // deuxième → rejet

        assertEquals(0, summary.successfullyImported());
        assertEquals(1, summary.errors().size());
        assertTrue(summary.errors().get(0).contains("Duplicate"));
    }

    @Test
    void shouldRejectInvalidCurrency() {
        repository.deleteAll();
        var request = new FxDealRequest("BAD-001", "USDD", "EUR", Instant.now(), BigDecimal.valueOf(100));

        var summary = service.importDeals(List.of(request));

        assertEquals(0, summary.successfullyImported());
        assertEquals(1, summary.errors().size());
        assertTrue(summary.errors().get(0).contains("ISO 4217"));
    }
}
