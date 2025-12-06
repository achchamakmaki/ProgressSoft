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
                // 1. Vérification du doublon
                if (repository.existsByDealUniqueId(req.dealUniqueId())) {
                    errors.add("Row " + processed + " → Duplicate dealUniqueId: " + req.dealUniqueId());
                    continue;
                }

                // 2. Validation des devises (ISO 4217 = 3 lettres exactement)
                String fromCurrency = req.fromCurrency().trim().toUpperCase();
                String toCurrency = req.toCurrency().trim().toUpperCase();

                if (fromCurrency.length() != 3 || !fromCurrency.matches("[A-Z]{3}")) {
                    errors.add("Row " + processed + " → Invalid fromCurrency '" + req.fromCurrency() + "' : must be exactly 3 uppercase letters");
                    continue;
                }
                if (toCurrency.length() != 3 || !toCurrency.matches("[A-Z]{3}")) {
                    errors.add("Row " + processed + " → Invalid toCurrency '" + req.toCurrency() + "' : must be exactly 3 uppercase letters");
                    continue;
                }

                // 3. Tout est OK → on crée l'entité
                FxDeal deal = FxDeal.builder()
                        .dealUniqueId(req.dealUniqueId())
                        .fromCurrency(fromCurrency)
                        .toCurrency(toCurrency)
                        .dealTimestamp(req.dealTimestamp())
                        .amount(req.amount())
                        .build();

                toSave.add(deal);

            } catch (Exception e) {
                // Attrape toute erreur inattendue pendant la construction
                errors.add("Row " + processed + " → Validation error: " + e.getMessage());
            }
        }

        // 4. Sauvegarde uniquement les lignes valides
        if (!toSave.isEmpty()) {
            try {
                repository.saveAll(toSave);
                log.info("Successfully imported {} deals", toSave.size());
            } catch (Exception e) {
                // En cas d'erreur DB (très rare maintenant qu’on a validé)
                errors.add("Database error during import: " + e.getMessage());
                log.error("Failed to save valid deals", e);
            }
        }

        return new ImportSummary(requests.size(), toSave.size(), errors);
    }
}
