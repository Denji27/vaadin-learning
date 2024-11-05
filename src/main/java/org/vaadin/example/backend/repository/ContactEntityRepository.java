package org.vaadin.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vaadin.example.backend.entity.ContactEntity;

import java.util.List;

@Repository
public interface ContactEntityRepository
        extends JpaRepository<ContactEntity, Long> {

    @Query("select c from ContactEntity c where :keyword = '' or (lower(c.firstName) like lower(concat('%', :keyword, '%'))  " +
            "or lower(c.lastName) like lower(concat('%', :keyword, '%'))) ")
    List<ContactEntity> searchByKeyword(String keyword);
}
