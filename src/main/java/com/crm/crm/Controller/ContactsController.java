package com.crm.crm.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.crm.Dto.LoginDto;
import com.crm.crm.Dto.LoginResponseDto;
import com.crm.crm.Dto.RegistrationDto;
import com.crm.crm.model.Contacts;
import com.crm.crm.repository.ContactsRepository;
import com.crm.crm.service.ContactsService;
import com.crm.crm.service.JwtService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/crm")
@CrossOrigin
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createContact(@RequestBody RegistrationDto newContact) {
        List<Contacts> extContact = this.contactsRepository.findByEmail(newContact.getEmail());
        List<Contacts> exContactsByUsername = this.contactsRepository.findByUsername(newContact.getUsername());

        if(extContact.size()>0 || exContactsByUsername.size()>0){
            return new ResponseEntity<>("Email or Username already exists", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.contactsService.addContact(newContact), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginDto loginDto){
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        if(authentication.isAuthenticated()){
            List<String> authorities = authentication.getAuthorities().stream()
                                                                .map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.toList());

            String role = authorities.size() > 1 ? String.join(",", authorities) : authorities.get(0);

            String token = this.jwtService.generateTokenForUser(loginDto.getUsername());

            LoginResponseDto response = new LoginResponseDto(role, token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
    }
    
    @GetMapping("/contacts/{id}")
    public ResponseEntity<?> getContact(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.contactsRepository.findById(id), HttpStatus.OK);
    }
    
    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllContacts(){
        return new ResponseEntity<>(this.contactsService.allContacts(), HttpStatus.OK);
    }

    @PutMapping("/contacts/edit/{id}")
    public ResponseEntity<?> editContact(@PathVariable("id") Long id, @RequestBody RegistrationDto newContact){
        Optional<Contacts> extContact = this.contactsRepository.findById(id);

        if(extContact.isPresent()){
            if(extContact.get().getEmail().equals(newContact.getEmail()) && extContact.get().getUsername().equals(newContact.getUsername())){
                return new ResponseEntity<>(this.contactsService.editContact(id, newContact), HttpStatus.OK);
            }else{
                List<Contacts> contactsWithSameEmail = this.contactsRepository.findByEmail(newContact.getEmail());
                List<Contacts> contactsWithSameUsername = this.contactsRepository.findByUsername(newContact.getUsername());

                if(contactsWithSameEmail.size()>0){
                    return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
                }else if(contactsWithSameUsername.size()>0){
                    return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
                }
                else{
                    return new ResponseEntity<>(this.contactsService.editContact(id, newContact), HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>("Id doesn't exists", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/contacts/delete/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") Long id){
        Optional<Contacts> extContact = this.contactsRepository.findById(id);

        if(extContact.isPresent() && this.contactsService.deleteContact(id)){
            return new ResponseEntity<>("Contact deleted successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Id doesn't exists", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/contacts/dashboard")
    public ResponseEntity<?> getUserdata(@RequestHeader("Authorization") String token){
        if(token != null){

            String filteredToken = token.substring(7);

            if(!this.jwtService.isTokenExpired(filteredToken)){
                String username = this.jwtService.extractUsernameFromToken(filteredToken);
                Contacts contact = this.contactsRepository.findByUsername(username).get(0);

                return new ResponseEntity<>(contact, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Token is expired", HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
    }
}
