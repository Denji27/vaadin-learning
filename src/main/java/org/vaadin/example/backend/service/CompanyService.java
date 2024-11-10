package org.vaadin.example.backend.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.domain.Company;
import org.vaadin.example.backend.domain.Contact;
import org.vaadin.example.backend.entity.CompanyEntity;
import org.vaadin.example.backend.entity.ContactEntity;
import org.vaadin.example.backend.mapper.CompanyEntityMapper;
import org.vaadin.example.backend.mapper.ContactEntityMapper;
import org.vaadin.example.backend.repository.CompanyEntityRepository;
import org.vaadin.example.backend.repository.ContactEntityRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyEntityRepository companyEntityRepository;
    private final CompanyEntityMapper companyEntityMapper;
    private final ContactEntityRepository contactEntityRepository;
    private final ContactEntityMapper contactEntityMapper;

    public List<Company> findAll() {
        List<Company> companies =
                this.companyEntityMapper.toDomain(
                        this.companyEntityRepository.findAll());
        List<Contact> contacts =
                this.contactEntityMapper.toDomain(
                        this.contactEntityRepository.findAll());
        for (Company company : companies) {
            List<Contact> contactList =
                    contacts.stream()
                            .filter(it -> Objects.equals(
                                    it.getCompanyId(), company.getId()))
                            .collect(Collectors.toList());
            company.enrichContacts(contactList);
        }
        return companies;
    }

    public Map<String, Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        this.findAll().forEach(c -> {
            stats.put(c.getName(), c.getContacts().size());
        });
        return stats;
    }

    @PostConstruct
    public void populateData() {
        if (this.companyEntityRepository.count() == 0) {
            this.companyEntityRepository.saveAll(
                    Stream.of("Path-way Electronics", "E-tech Management", "Path-e-tech Management")
                            .map(CompanyEntity::new)
                            .collect(Collectors.toList()));
        }

        if (this.contactEntityRepository.count() == 0) {
            Random r  = new Random(0);
            List<Company> companies =
                    this.companyEntityMapper.toDomain(
                            this.companyEntityRepository.findAll());
            this.contactEntityRepository.saveAll(
                    Stream.of("Gabriel Patel", "Brian Robinson", "Eduardo Haugen",
                            "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustaysson",
                            "Emily Stewart", "Corinne Davis", "Ryan Davis", "Yurem Jackson", "Kelly Gustavo",
                            "Danielle Watson", "Katelyn Martin", "Gunner Karsen", "Jamar Olsson", "Jadan Jackson")
                            .map(name -> {
                                String[] nameParts = name.split(" ");
                                Contact contact = new Contact();
                                contact.setFirstName(nameParts[0]);
                                contact.setLastName(nameParts[1]);
                                contact.setCompanyId(companies.get(r.nextInt(companies.size())).getId());
                                contact.setStatus(ContactEntity.Status.values()[r.nextInt(ContactEntity.Status.values().length)]);
                                String email = contact.getFirstName() + "." + contact.getLastName() + "@gmail.com";
                                contact.setEmail(email);
                                return this.contactEntityMapper.toEntity(contact);
                            }).collect(Collectors.toList())
            );
        }
    }
}
