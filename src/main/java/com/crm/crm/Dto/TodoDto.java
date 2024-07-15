package com.crm.crm.Dto;

import lombok.Data;

@Data
public class TodoDto {
    private String description;
    private boolean enable;

    public TodoDto(){

    }

    public TodoDto(String description, boolean enable) {
        this.description = description;
        this.enable = enable;
    }
}
