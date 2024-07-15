package com.crm.crm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "todos")
public class Todos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name ="enable")
    private boolean enable;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @JsonBackReference
    private Contacts contact;

    public Todos(){

    }

    public Todos(Contacts cont, boolean enable, String desc){
        this.contact = cont;
        this.enable = enable;
        this.description = desc;
    }
}
