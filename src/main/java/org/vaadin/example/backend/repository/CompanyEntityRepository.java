package org.vaadin.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vaadin.example.backend.entity.CompanyEntity;

@Repository
public interface CompanyEntityRepository
        extends JpaRepository<CompanyEntity, Long> {
}
