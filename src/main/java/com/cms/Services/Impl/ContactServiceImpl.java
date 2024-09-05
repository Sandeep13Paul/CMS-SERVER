package com.cms.Services.Impl;

import com.cms.Helpers.ResourceNotFoundException;
import com.cms.Models.Contact;
import com.cms.Models.User;
import com.cms.Repositories.ContactRepository;
import com.cms.Repositories.userRepository;
import com.cms.Services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private userRepository userRepository;

    @Override
    public Contact saveContact(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setContactId(contactId);
        return this.contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact contact1 = this.contactRepository.findById(contact.getContactId()).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        contact1.setName(contact.getName());
        contact1.setEmail(contact.getEmail());
        contact1.setAbout(contact.getAbout());
        contact1.setAddress(contact.getAddress());
        contact1.setPhone(contact.getPhone());
        contact1.setFavorite(contact.isFavorite());
        contact1.setWebsiteLink(contact.getWebsiteLink());
        contact1.setLinkedInLink(contact.getLinkedInLink());
        contact1.setContactPic(contact.getContactPic());

        return this.contactRepository.save(contact1);
    }

    @Override
    public List<Contact> getAllContacts() {
        return this.contactRepository.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return this.contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Not Found"));
    }

    @Transactional
    @Override
    public void deleteContact(String id) {
        try {
            System.out.println("Attempting to find contact with ID: " + id);
            Contact contact = this.contactRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

            System.out.println("Contact found: " + contact);
            System.out.println("Attempting to delete contact...");
            this.contactRepository.deleteByContactId(id);

            // Check if the contact is really deleted
            boolean exists = this.contactRepository.existsById(id);
            System.out.println("Contact deletion status: " + (exists ? "Failed" : "Successful"));

        } catch (Exception e) {
            System.err.println("Error during deletion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Page<Contact> searchContact(User user, String field, String keyword, int page, int size, String sortDirection) {

        Page<Contact> contactsPage = null;
        Sort sort = sortDirection.equals("desc") ? Sort.by(field).descending() : Sort.by(field).ascending();
        var pageable = PageRequest.of(page, size, sort);

        if (field.equalsIgnoreCase("name")) {

            contactsPage = this.contactRepository.findContactsByContactName(user, keyword, pageable);
        }
        else if (field.equalsIgnoreCase("email")) {

            contactsPage = this.contactRepository.findContactsByContactEmail(user, keyword, pageable);
        }
        else if (field.equalsIgnoreCase("phone")) {

            contactsPage = this.contactRepository.findContactsByContactPhone(user, keyword, pageable);
        }
        return contactsPage;
    }

    @Override
    public List<Contact> getContactByUser(User user) {
        return this.contactRepository.findByUser(user);
    }

    @Override
    public Page<Contact> getContactsByUser(User user, int page, int size, String sortField, String sortDirection) {

        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        var pageable = PageRequest.of(page, size, sort);

        // Debugging: Check user details
        System.out.println("User ID: " + user.getUserId());
        System.out.println("User Email: " + user.getEmail());

        Page<Contact> contactsPage = this.contactRepository.findContactsByUser(user, pageable);

        System.out.println("Page: " + page);
        System.out.println("Size: " + size);
        System.out.println("Sort Field: " + sortField);
        System.out.println("Sort Direction: " + sortDirection);

        System.out.println("I am inside contact service impl");
        contactsPage.getContent().forEach(contact -> System.out.println("Contact Name: " + contact.getName()));

        return contactsPage;
    }
}
