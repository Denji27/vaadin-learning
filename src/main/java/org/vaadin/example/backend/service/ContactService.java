package org.vaadin.example.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.repository.CompanyRepository;
import org.vaadin.example.backend.repository.ContactRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
}
