package com.nobless.quittances.repository;

import com.nobless.quittances.model.Quittance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuittanceRepository extends JpaRepository<Quittance, Long> {
    List<Quittance> findByIdProprio(Long idProprio);
}
