package com.cms.Repositories;

import com.cms.Models.Contact;
import com.cms.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
//    find the contacts by user
    @Query("select c from Contact c where c.user = :user")
    Page<Contact> findContactsByUser(@Param("user") User user, Pageable pageable);

    //find contact based on name
    @Query("select c from Contact c where c.user = :user and c.name like %:name%")
    Page<Contact> findContactsByContactName(@Param("user") User user, @Param("name") String name , Pageable pageable);

//    find contact based on email
    @Query("select c from Contact c where c.user = :user and c.email like %:email%")
    Page<Contact> findContactsByContactEmail(@Param("user") User user, @Param("email") String email , Pageable pageable);

//    find contact based on phone
    @Query("select c from Contact c where c.user = :user and c.phone like %:phone%")
    Page<Contact> findContactsByContactPhone(@Param("user") User user, @Param("phone") String phone , Pageable pageable);

//    @Query("select c from Contact c where c.userId = :userId")
    List<Contact> findByUser(User user);

//    @Query("select c from Contact c where c.name like %:name%")
//    List<Contact> findByContactName(@Param("name") String name);

    @Modifying
    @Query("delete from Contact c where c.contactId = :contactId")
    void deleteByContactId(@Param("contactId") String contactId);

}
