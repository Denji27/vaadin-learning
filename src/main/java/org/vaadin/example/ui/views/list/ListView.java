package org.vaadin.example.ui.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.example.backend.domain.Company;
import org.vaadin.example.backend.domain.Contact;
import org.vaadin.example.backend.service.CompanyService;
import org.vaadin.example.backend.service.ContactService;
import org.vaadin.example.ui.MainLayout;

import java.util.List;
import java.util.Objects;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Contact | Vaadin CRM")
@RolesAllowed("ADMIN")
//@CssImport("./themes/my-theme/styles.css")
public class ListView extends VerticalLayout {

//    public MainView() {
//        Button button = new Button("Click me");
//
//        DatePicker datePicker = new DatePicker("Pick a date");
//        HorizontalLayout layout = new HorizontalLayout(button, datePicker);
//        layout.setDefaultVerticalComponentAlignment(Alignment.END);
//        add(layout);
//
//        button.addClickListener(click ->
//                add(new Paragraph("Clicked: " + datePicker.getValue())));
//    }

    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    private final ContactService contactService;
    private final CompanyService companyService;
    private final ContactForm form;

    public ListView(
            ContactService contactService,
            CompanyService companyService,
            AuthenticationContext authContext) {
        this.contactService = contactService;
        this.companyService = companyService;
        System.out.println(authContext.getAuthenticatedUser(UserDetails.class).get().getAuthorities());
        addClassName("list-view");
        setSizeFull();
        this.configureGrid();
        this.form = new ContactForm(this.findAllCompany());
        this.form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        this.form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        this.form.addListener(ContactForm.CloseEvent.class, evt -> closeEditor());

        Div content = new Div(this.grid, this.form);
        content.addClassName("content");
        content.setSizeFull();

        add(this.getToolBar(), content);

        this.updateList();
        this.closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent deleteEvent) {
        Contact contact = deleteEvent.getContact();
        this.contactService.delete(contact);
        this.updateList();
        this.closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent saveEvent) {
        Contact contact = saveEvent.getContact();
        contact.setCompanyId(contact.getCompany().getId());
        this.contactService.create(contact);
        this.updateList();
        this.closeEditor();
    }

    private void closeEditor() {
        this.form.setContact(null);
        this.form.setVisible(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        this.grid.addClassName("contact-grid");
        this.grid.setSizeFull();
        this.grid.removeColumnByKey("company");
        this.grid.setColumns("firstName", "lastName", "email", "status");
        this.grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return Objects.isNull(company) ? "-" : company.getName();
        }).setHeader("Company");
        this.grid.getColumns().forEach(col -> col.setAutoWidth(true));

        this.grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
    }

    private void editContact(Contact contact) {
        if (Objects.isNull(contact)) {
            closeEditor();
        } else {
            this.form.setContact(contact);
            this.form.setVisible(true);
        }
    }

    private HorizontalLayout getToolBar() {
        this.filterText.setPlaceholder("Filter by name...");
        this.filterText.setClearButtonVisible(true);
        this.filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact", click -> this.addContact());
        HorizontalLayout toolbar = new HorizontalLayout(this.filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        this.grid.asSingleSelect().clear();
        this.editContact(new Contact());
    }

    private void updateList() {
        this.grid.setItems(this.contactService.searchByKeyWord(filterText.getValue()));
    }

    private List<Company> findAllCompany() {
        return this.companyService.findAll();
    }
}
