package com.crm.crm.Dto;

import lombok.Data;

@Data
public class RegistrationDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String roles;

    public RegistrationDto(){

    }

    public RegistrationDto(String username, String firstName, String lastName, String email, String password, String roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}