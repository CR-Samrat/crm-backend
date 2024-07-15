package com.crm.crm.service;

import java.util.List;

import com.crm.crm.Dto.RegistrationDto;
import com.crm.crm.model.Contacts;

public interface ContactsService {
    Contacts addContact(RegistrationDto newContact);
    List<Contacts> allContacts();
    Contacts editContact(Long id, RegistrationDto newContact);
    boolean deleteContact(Long id);
}
