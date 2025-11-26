package com.progresssoft.deal.dto;

import java.util.List;

public record ImportSummary(
        int totalProcessed,
        int successfullyImported,
        List<String> errors
) {}
