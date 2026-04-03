package com.nobless.quittances.repository;

import com.nobless.quittances.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByLogin(String login);
}
