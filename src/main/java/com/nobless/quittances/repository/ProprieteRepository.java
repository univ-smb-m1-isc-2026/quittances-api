package com.nobless.quittances.repository;

import com.nobless.quittances.model.Propriete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProprieteRepository extends JpaRepository<Propriete, Long> {
	List<Propriete> findByIdProprios(Long idProprios);
}
