package com.crm.crm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crm.crm.Dto.RegistrationDto;
import com.crm.crm.model.Contacts;
import com.crm.crm.model.Todos;
import com.crm.crm.repository.ContactsRepository;
import com.crm.crm.service.ContactsService;

@Service
@Transactional
public class ContactsServiceImpl implements ContactsService{

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Contacts addContact(RegistrationDto newContact){
        Contacts contact = new Contacts();
        contact.setUsername(newContact.getUsername());
        contact.setEmail(newContact.getEmail());
        contact.setFirstName(newContact.getFirstName());
        contact.setLastName(newContact.getLastName());
        contact.setPassword(this.passwordEncoder.encode(newContact.getPassword()));
        contact.setRoles(newContact.getRoles());

        List<Todos> tasks = new ArrayList<>();
        contact.setTodos(tasks);

        return this.contactsRepository.save(contact);
    }

    @Override
    public List<Contacts> allContacts() {
        return this.contactsRepository.findAll();
    }

    @Override
    public Contacts editContact(Long id, RegistrationDto newContact) {
        Optional<Contacts> extContact = this.contactsRepository.findById(id);

        Contacts editedContact = extContact.get();
        editedContact.setUsername(newContact.getUsername());
        editedContact.setEmail(newContact.getEmail());
        editedContact.setFirstName(newContact.getFirstName());
        editedContact.setLastName(newContact.getLastName());
        editedContact.setPassword(this.passwordEncoder.encode(newContact.getPassword()));
        editedContact.setRoles(newContact.getRoles());

        return this.contactsRepository.save(editedContact);
    }

    @Override
    public boolean deleteContact(Long id) {
        this.contactsRepository.deleteById(id);
        return true;
    }
    
}
