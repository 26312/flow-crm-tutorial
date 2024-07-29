package com.example.application.views.list;

import com.example.application.data.Company;
import com.example.application.data.Contact;
import com.example.application.data.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ContactForm extends FormLayout {
    /**
     * Variable fields, below are case-sensitive to @Contact class.
     * This is done so that Vaadin binders can be utilized
     * to bind them together automatically.
     */

    // Binder is something Vaadin uses for Binding between a model object and UI Components
     // BeanValidationBinder will use BeanValidation Annotations on the class that we have to provide validation  in the UI
    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    EmailField email = new EmailField("Email");

    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Contact contact;

    public ContactForm(List<Company> companies, List<Status> statuses ){
        addClassName("contact-form");

        binder.bindInstanceFields(this);

        company.setItems(companies);
        //properties to show as visible properties
        company.setItemLabelGenerator(Company::getName);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        add(
                firstName,
                lastName,
                email,
                company,
                status,
                createButtonsLayout()
        );

    }

    public void setContact(Contact contact){
        this.contact = contact;
        binder.readBean(contact);
    }
    private Component createButtonsLayout() {
       save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
       cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

       save.addClickListener(event->validateAndSave());
       delete.addClickListener(event-> fireEvent(new DeleteEvent(this, contact)));
       cancel.addClickListener(event-> fireEvent(new CloseEvent(this)));
       // Add shortCuts for people using Key-board
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save,delete,cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(contact);
            fireEvent(new SaveEvent(this, contact));
        }catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
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

    public<T extends ComponentEvent <?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
            return getEventBus().addListener(eventType,listener);
    }

  /*  public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }*/


}
