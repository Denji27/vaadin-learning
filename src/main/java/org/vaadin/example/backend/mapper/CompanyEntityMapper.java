package org.vaadin.example.backend.mapper;

import org.mapstruct.Mapper;
import org.vaadin.example.backend.domain.Company;
import org.vaadin.example.backend.entity.CompanyEntity;
import org.vaadin.example.common.mapper.EntityMapper;

@Mapper(componentModel = "Spring")
public interface CompanyEntityMapper extends EntityMapper<Company, CompanyEntity> {
}
