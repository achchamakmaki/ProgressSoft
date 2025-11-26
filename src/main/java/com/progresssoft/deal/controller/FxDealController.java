package com.progresssoft.deal.controller;

import com.progresssoft.deal.dto.FxDealRequest;
import com.progresssoft.deal.dto.ImportSummary;
import com.progresssoft.deal.service.FxDealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fx-deals")
@RequiredArgsConstructor
@Slf4j
public class FxDealController {

    private final FxDealService fxDealService;

    @PostMapping
    public ResponseEntity<ImportSummary> importDeals(@Valid @RequestBody List<FxDealRequest> deals) {
        log.info("Received request to import {} deal(s)", deals.size());
        ImportSummary summary = fxDealService.importDeals(deals);
        return ResponseEntity.status(HttpStatus.CREATED).body(summary);
    }

    // Endpoint bonus (pas demandé mais très apprécié)
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("FX Deals Importer is UP & RUNNING");
    }
}
