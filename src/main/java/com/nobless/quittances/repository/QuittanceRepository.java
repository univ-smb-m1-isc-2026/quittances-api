package com.nobless.quittances.repository;

import com.nobless.quittances.model.Quittance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuittanceRepository extends JpaRepository<Quittance, Long> {
    List<Quittance> findByProprioId(Long proprioId);
    boolean existsByProprieteIdAndPeriod(Long proprieteId, String period);
    Optional<Quittance> findByProprieteIdAndPeriod(Long proprieteId, String period);
}
