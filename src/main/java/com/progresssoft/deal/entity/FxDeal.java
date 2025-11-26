package com.progresssoft.deal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fx_deals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FxDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deal_unique_id", nullable = false, unique = true, length = 50)
    private String dealUniqueId;

    @Column(name = "from_currency", nullable = false, length = 3)
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false, length = 3)
    private String toCurrency;

    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;

    @Column(name = "amount", nullable = false, precision = 20, scale = 8)
    private BigDecimal amount;
}
