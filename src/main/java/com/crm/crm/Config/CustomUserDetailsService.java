package com.crm.crm.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.crm.crm.model.Contacts;
import com.crm.crm.repository.ContactsRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private ContactsRepository contactsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Contacts> contact = this.contactsRepository.findByUsername(username);

        if(contact.size()==1){
            return new CustomUserDetails(contact.get(0));
        }else{
            throw new UsernameNotFoundException("Invalid username");
        }
    }
    
}
