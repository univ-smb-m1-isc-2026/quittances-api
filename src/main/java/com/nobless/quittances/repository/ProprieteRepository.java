package com.nobless.quittances.repository;

import com.nobless.quittances.model.Propriete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProprieteRepository extends JpaRepository<Propriete, Long> {
	List<Propriete> findByIdProprio(Long idProprio);
	Optional<Propriete> findByAdresse(String adresse);
	void deleteByIdProprio(Long idProprio);
}
