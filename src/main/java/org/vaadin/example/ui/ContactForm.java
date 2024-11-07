package org.vaadin.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.example.backend.domain.Company;
import org.vaadin.example.backend.domain.Contact;
import org.vaadin.example.backend.entity.ContactEntity;

import java.util.List;

public class ContactForm extends FormLayout {

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField email = new TextField("Email");
    ComboBox<ContactEntity.Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");

    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    public ContactForm(List<Company> companies) {
        addClassName("contact-form");
        this.binder.bindInstanceFields(this);
        this.status.setItems(ContactEntity.Status.values());
        this.company.setItems(companies);
        this.company.setItemLabelGenerator(Company::getName);


        add(
                this.firstName,
                this.lastName,
                this.email,
                this.status,
                this.company,
                this.createButtonsLayout());
    }

    public void setContact(Contact contact) {
        this.binder.setBean(contact);
    }

    private Component createButtonsLayout() {
        this.save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        this.save.addClickShortcut(Key.ENTER);
        this.close.addClickShortcut(Key.ESCAPE);

        this.save.addClickListener(click -> validateAndSave());
        this.delete.addClickListener(
                click -> fireEvent(new DeleteEvent(this, this.binder.getBean())));
        this.close.addClickListener(
                click -> fireEvent(new CloseEvent(this)));

        this.addSingleClickListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(this.save, this.delete, this.close);
    }

    private void validateAndSave() {
        if (this.binder.isValid()) {
            fireEvent(new SaveEvent(this, this.binder.getBean()));
        }
    }

    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;
        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return this.contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
