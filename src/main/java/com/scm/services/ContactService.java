package com.scm.services;

import java.util.List;

import com.scm.entities.Contact;

public interface ContactService {
    // save contact
    Contact save(Contact contact);

    //update contact
    Contact update(Contact contact);

    //get contacts

    List<Contact>getAll();

    //get contact by id

    Contact getById(String id);

    //delete Contact
    void delete(String id);

    //search Contact
    List<Contact> search(String name,String email,String phoneNumber);

    //get contact by userId

    List<Contact>getByUserId(String userId);
}
