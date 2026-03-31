package com.nobless.quittances.repository;

import com.nobless.quittances.model.Proprio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprioRepository extends JpaRepository<Proprio, Long> {
	Proprio findByEmail(String email);
}
