package org.vaadin.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.vaadin.example.common.entity.AbstractEntity;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "contract")
public class ContactEntity extends AbstractEntity implements Cloneable {

    @Override
    public ContactEntity clone() {
        try {
            // TODO: copy mutable state here,
            //  so the clone can't change the internals of the original
            return (ContactEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public enum Status {
        IMPORTED_LEAD, NOT_CONTRACTED, CONTRACTED, CUSTOMER, CLOSED_LOST
    }

    @NotNull
    @NotEmpty
    @Column(name = "first_name", length = 50)
    private String firstName = "";

    @NotNull
    @NotEmpty
    @Column(name = "last_name", length = 50)
    private String lastName = "";

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status;

    @Email
    @NotNull
    @NotEmpty
    @Column(name = "email", length = 200)
    private String email = "";

    @NotNull
    @Column(name = "company_id")
    private Long companyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ContactEntity that = (ContactEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
