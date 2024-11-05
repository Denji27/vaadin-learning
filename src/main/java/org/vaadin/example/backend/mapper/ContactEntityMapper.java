package org.vaadin.example.backend.mapper;

import org.mapstruct.Mapper;
import org.vaadin.example.backend.domain.Contact;
import org.vaadin.example.backend.entity.ContactEntity;
import org.vaadin.example.common.mapper.EntityMapper;

@Mapper(componentModel = "Spring")
public interface ContactEntityMapper extends EntityMapper<Contact, ContactEntity> {
}
