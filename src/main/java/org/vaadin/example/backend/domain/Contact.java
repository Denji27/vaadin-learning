package org.vaadin.example.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.vaadin.example.backend.entity.ContactEntity;
import org.vaadin.example.common.domain.AbstractDomain;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Contact extends AbstractDomain {

    private String firstName;
    private String lastName;
    private ContactEntity.Status status;
    private String email;
    private Long companyId;
    private Company company;

    public void enrichCompany(Company company) {
        this.company = company;
    }
}
