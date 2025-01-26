package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;
import com.scm.entities.User;

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

    Page<Contact>getByUser(User user,int page,int size,String sortField,String direction);
}
