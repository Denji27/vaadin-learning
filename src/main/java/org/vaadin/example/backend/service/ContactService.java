package org.vaadin.example.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.vaadin.example.backend.domain.Company;
import org.vaadin.example.backend.domain.Contact;
import org.vaadin.example.backend.entity.CompanyEntity;
import org.vaadin.example.backend.mapper.CompanyEntityMapper;
import org.vaadin.example.backend.mapper.ContactEntityMapper;
import org.vaadin.example.backend.repository.CompanyEntityRepository;
import org.vaadin.example.backend.repository.ContactEntityRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {
    private final ContactEntityRepository contactEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final ContactEntityMapper contactEntityMapper;
    private final CompanyEntityMapper companyEntityMapper;

    public List<Contact> findAll() {
        List<Company> companies =
                this.companyEntityMapper.toDomain(
                        this.companyEntityRepository.findAll());
        List<Contact> contacts =
                this.contactEntityMapper.toDomain(
                        this.contactEntityRepository.findAll());
        for (Contact contact : contacts) {
            Optional<Company> company =
                    companies.stream()
                            .filter(it -> Objects.equals(
                                    it.getId(), contact.getCompanyId()))
                            .findFirst();
            company.ifPresent(contact::enrichCompany);
        }
        return contacts;
    }

    public List<Contact> searchByKeyWord(String keyword) {
        List<Company> companies =
                this.companyEntityMapper.toDomain(
                        this.companyEntityRepository.findAll());
        List<Contact> contacts =
                this.contactEntityMapper.toDomain(
                        this.contactEntityRepository.searchByKeyword(keyword));
        for (Contact contact : contacts) {
            Optional<Company> company =
                    companies.stream()
                            .filter(it -> Objects.equals(
                                    it.getId(), contact.getCompanyId()))
                            .findFirst();
            company.ifPresent(contact::enrichCompany);
        }
        return contacts;
    }

    public long count() {
        return this.contactEntityRepository.count();
    }

    public void delete(Contact contact) {
        this.contactEntityRepository.delete(this.contactEntityMapper.toEntity(contact));
    }

    public void create(Contact contact) {
        if (Objects.isNull(contact)) {
            log.info("Contact is null!!");
            return;
        }
        if (Objects.isNull(contact.getCompanyId())) {
            log.info("Company id is null!!");
            return;
        }
        Optional<CompanyEntity> companyEntityOptional =
                this.companyEntityRepository.findById(contact.getCompanyId());
        if (companyEntityOptional.isEmpty()) {
            log.info("Company is not found!!");
            return;
        }
        this.contactEntityRepository.save(this.contactEntityMapper.toEntity(contact));
    }
}
