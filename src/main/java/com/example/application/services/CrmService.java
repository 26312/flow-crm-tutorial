package com.example.application.services;

import com.example.application.data.Company;
import com.example.application.data.Contact;
import com.example.application.data.Status;
import com.example.application.repository.CompanyReposiotry;
import com.example.application.repository.ContactRepository;
import com.example.application.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This Service class will essentially have all the api that would be
 * used to connect to DB with the application
 */
@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyReposiotry companyReposiotry;
    private final StatusRepository statusRepository;

    public CrmService(ContactRepository contactRepository,
                      CompanyReposiotry companyReposiotry,
                      StatusRepository statusRepository){
        this.contactRepository = contactRepository;
        this.companyReposiotry = companyReposiotry;
        this.statusRepository = statusRepository;
    }

    /**
     * Method returns entire contact list from database if filterText
     * is not supplied, else contact list based on passed value is returned.
     *
     * @param filterText
     * @return
     */
    public List<Contact> findAllContact(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return contactRepository.findAll();
        }else{
            return contactRepository.search(filterText);
        }
    }

    /**
     * Returns the count of contacts from DB
     * @return
     */
    public long countContact(){
        return contactRepository.count();
    }

    /**
     * Deletes the contact based on passed contact, required to be deleted
     * @param contact
     */
    public void deleteContact(Contact contact){
        contactRepository.delete(contact);
    }

    /**
     * Saves contact in DB, based on passed Contact details
     * @param contact
     */
    public void saveContact(Contact contact){
        if(contact == null){
            System.err.println("Contact is null...");
            return;
        }
        contactRepository.save(contact);
    }


    public List<Company> findAllCompanies(){
        return companyReposiotry.findAll();
    }

    public List<Status> findAllStatus(){
        return statusRepository.findAll();
    }
}
