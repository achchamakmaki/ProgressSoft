package com.progresssoft.deal.service;

import com.progresssoft.deal.dto.FxDealRequest;
import com.progresssoft.deal.dto.ImportSummary;
import com.progresssoft.deal.entity.FxDeal;
import com.progresssoft.deal.repository.FxDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxDealService {

    private final FxDealRepository repository;

    @Transactional
    public ImportSummary importDeals(List<FxDealRequest> requests) {
        List<FxDeal> toSave = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int processed = 0;

        for (FxDealRequest req : requests) {
            processed++;
            try {
                // 1. Idempotence : on refuse les doublons
                if (repository.existsByDealUniqueId(req.dealUniqueId())) {
                    errors.add("Row " + processed + " → Duplicate dealUniqueId: " + req.dealUniqueId());
                    continue;
                }

                FxDeal deal = FxDeal.builder()
                        .dealUniqueId(req.dealUniqueId())
                        .fromCurrency(req.fromCurrency().toUpperCase())
                        .toCurrency(req.toCurrency().toUpperCase())
                        .dealTimestamp(req.dealTimestamp())
                        .amount(req.amount())
                        .build();

                toSave.add(deal);
            } catch (Exception e) {
                errors.add("Row " + processed + " → Validation error: " + e.getMessage());
            }
        }

        if (!toSave.isEmpty()) {
            repository.saveAll(toSave);
            log.info("Successfully imported {} deals", toSave.size());
        }

        return new ImportSummary(processed, toSave.size(), errors);
    }
}
