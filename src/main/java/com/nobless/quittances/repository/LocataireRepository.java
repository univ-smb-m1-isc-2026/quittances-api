package com.nobless.quittances.repository;

import com.nobless.quittances.model.Locataire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocataireRepository extends JpaRepository<Locataire, Long> {
    
}
