package com.cms.Services;

import com.cms.Models.Contact;
import com.cms.Models.User;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ContactService {
    //save contacts
    public Contact saveContact(Contact contact);

    //update Contact
    public Contact updateContact(Contact contact);

    //get contacts
    List<Contact> getAllContacts();

    //get contact by id
    Contact getContactById(String id);

    //delete contact
    void deleteContact(String id);

    //search contact
    List<Contact> searchContact(String name, String email, String phone);

    //get contacts by userId
    List<Contact> getContactByUser(User user);

    Page<Contact> getContactsByUser(User user, int page, int size, String sortField, String sortDirection);
}
