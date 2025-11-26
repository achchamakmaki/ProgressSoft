package com.progresssoft.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record FxDealRequest(

        @NotBlank(message = "Deal Unique ID is required")
        @Size(max = 50)
        String dealUniqueId,

        @NotBlank @Pattern(regexp = "^[A-Z]{3}$", message = "From currency must be valid ISO 4217 code")
        String fromCurrency,

        @NotBlank @Pattern(regexp = "^[A-Z]{3}$", message = "To currency must be valid ISO 4217 code")
        String toCurrency,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X", timezone = "UTC")
        Instant dealTimestamp,

        @NotNull @Positive(message = "Amount must be positive")
        BigDecimal amount

) {}
