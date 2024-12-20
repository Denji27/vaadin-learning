package org.vaadin.example.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.vaadin.example.common.domain.AbstractDomain;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Company extends AbstractDomain {

    private String name;
    private List<Contact> contacts;

    public void enrichContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
