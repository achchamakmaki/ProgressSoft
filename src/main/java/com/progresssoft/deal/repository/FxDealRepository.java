package com.progresssoft.deal.repository;

import com.progresssoft.deal.entity.FxDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FxDealRepository extends JpaRepository<FxDeal, Long> {
    boolean existsByDealUniqueId(String dealUniqueId);
    Optional<FxDeal> findByDealUniqueId(String dealUniqueId);
}
