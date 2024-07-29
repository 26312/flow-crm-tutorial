package com.example.application.views.list;


import com.example.application.data.Contact;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.Collections;


@PageTitle("Contacts | Vaadin CRM") // This will be the page title of the browser
@Route(value = "")
@RouteAlias(value = "")
public class ListView extends VerticalLayout  {

    private final CrmService service;
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();

    ContactForm form;

    public ListView(CrmService service)  {
        this.service = service;

        /**Whenever creating a new view call/give a class name so in case
         * a CSS is being created later, the class name can be used
         */
        addClassName("list-view");
        setSizeFull();// will make the view the same size as the entire browser window
        configureGrid();
        configureForm();
        add(
          getToolbar(),
          //grid
          getContent()
        );

        updateList();
    }

    /**
     * Method will fetch the details from database for the values
     * present in the filterText
     */
    private void updateList() {
        grid.setItems(service.findAllContact(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content=new HorizontalLayout(grid,form);
        content.setFlexGrow(2,grid);
        content.setFlexGrow(1,form);
        content.addClassName("content");
        content.setSizeFull();
        return content;

    }

    private void configureForm() {
        //form = new ContactForm(Collections.emptyList(),Collections.emptyList());
        form = new ContactForm(service.findAllCompanies(),service.findAllStatus());
        form.setWidth("25em");
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e->updateList());
        Button addContactButton = new Button("Add Contact");
        HorizontalLayout  toolBar =new HorizontalLayout(filterText, addContactButton);
        toolBar.addClassName("toolBar");
        return toolBar;
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName","lastName","email");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader(("Status"));
        grid.getColumns().forEach(col->col.setAutoWidth(true));
    }

}
