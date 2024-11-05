package org.vaadin.example.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.vaadin.example.backend.domain.Company;
import org.vaadin.example.backend.domain.Contact;
import org.vaadin.example.backend.service.ContactService;

import java.util.Objects;

@Route("")
public class MainView extends VerticalLayout {

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

    public MainView(ContactService contactService) {
        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();
        this.configureGrid();

        add(this.filterText, this.grid);
        this.configureFilter();
        this.updateList();
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
    }

    private void configureFilter() {
        this.filterText.setPlaceholder("Filter by name...");
        this.filterText.setClearButtonVisible(true);
        this.filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }
    private void updateList() {
        this.grid.setItems(this.contactService.searchByKeyWord(filterText.getValue()));
    }
}
