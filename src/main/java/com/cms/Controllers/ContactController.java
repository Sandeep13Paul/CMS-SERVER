package com.cms.Controllers;

import com.cms.Forms.ContactForm;
import com.cms.Helpers.Helper;
import com.cms.Helpers.Message;
import com.cms.Models.Contact;
import com.cms.Models.User;
import com.cms.Services.ContactService;
import com.cms.Services.ImageService;
import com.cms.Services.userService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private userService userService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/add")
    public ResponseEntity<String> addContact(@Valid ContactForm contactForm, @RequestParam("contactPic") MultipartFile file, Authentication authentication, BindingResult result, HttpSession session) {
        System.out.println("Processing Contact Register");

        if (result.hasErrors()) {
            // Return a single error message as a string
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(400).body(errorMessage);
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = this.userService.getUserByEmail(username);


        // Save contact details to the database
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAbout(contactForm.getAbout());
        contact.setPhone(contactForm.getPhone());

        if (contactForm.getContactPic() != null && !contactForm.getContactPic().isEmpty()) {
            System.out.println("Contact Image = " + file.getOriginalFilename());

            String fileURL = imageService.uploadImage(file);
            contact.setContactPic(fileURL);
        }

        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedinLink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setUser(user);

        Contact savedContact = this.contactService.saveContact(contact);
        System.out.println("Contact saved: " + savedContact);

        Message message = Message
                .builder()
                .content("Contact Added Successful")
                .type("green")
                .build();

        // Set message in session
        session.setAttribute("message", message);
        System.out.println("Message set in session: " + session.getAttribute("message"));
        System.out.println("Session ID: " + session.getId());

        // Return the token to the frontend
        return ResponseEntity.ok("Contact Added Successfully");
    }

    @GetMapping("/session-message")
    public ResponseEntity<Message> getSessionMessage(HttpSession session) {
        Message toastMessage = (Message) session.getAttribute("message");
        System.out.println("Session ID: " + session.getId());
        System.out.println("Message retrieved from session: " + toastMessage);

        // Remove the message from session after retrieving it
        session.removeAttribute("message");

        // Return the message, or an empty string if it's null
        return ResponseEntity.ok(toastMessage);
    }

    @PostMapping("/show")
    public ResponseEntity<?> showContacts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "5") int size, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "direction", defaultValue = "asc") String sortDirection, Authentication authentication) {
        System.out.println("Show contacts::");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        System.out.println(username);
        User user = this.userService.getUserByEmail(username);
        System.out.println("User ID: " + user.getUserId());
        System.out.println("User Email: " + user.getEmail());


        Page<Contact> pageContact = this.contactService.getContactsByUser(user, page, size, sortBy, sortDirection);



        return ResponseEntity.ok(pageContact);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContact(@RequestParam(value = "id") String contactId, Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        System.out.println(username);
        System.out.println(contactId);
        this.contactService.deleteContact(contactId);
        System.out.println("After deleting contacts");

        return ResponseEntity.ok("deleted");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateContact(@Valid ContactForm contactForm, @RequestParam(value = "id") String contactId, @RequestParam(value = "contactPic") MultipartFile file, Authentication authentication) {
        System.out.println("Update form");
        Contact contact1 = new Contact();

        if (contactForm.getContactPic() != null && !contactForm.getContactPic().isEmpty()) {
            String fileURL = imageService.uploadImage(file);
            contact1.setContactPic(fileURL);
        }

        contact1.setContactId(contactId);
        contact1.setName(contactForm.getName());
        contact1.setEmail(contactForm.getEmail());
        contact1.setAbout(contactForm.getAbout());
        contact1.setAddress(contactForm.getAddress());
        contact1.setPhone(contactForm.getPhone());
        contact1.setFavorite(contactForm.isFavorite());
        contact1.setWebsiteLink(contactForm.getWebsiteLink());
        contact1.setLinkedInLink(contactForm.getLinkedinLink());

        Contact contact = this.contactService.updateContact(contact1);
        System.out.println("After updation");

        return ResponseEntity.ok("Updated");
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchContact(@RequestParam("field") String field, @RequestParam("keyword") String keyword, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "5") int size, @RequestParam(value = "direction", defaultValue = "asc") String sortDirection, Authentication authentication) {
        System.out.println("I am inside search backend");
        System.out.println(field);
        System.out.println(keyword);

        String username = Helper.getEmailOfLoggedInUser(authentication);
        System.out.println(username);
        User user = this.userService.getUserByEmail(username);

        Page<Contact> contacts = this.contactService.searchContact(user, field, keyword, page, size, sortDirection);
        return ResponseEntity.ok(contacts);
    }

}
