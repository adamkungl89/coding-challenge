package com.adamspokes.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    
    REVENUE("revenue"),
    EXPENSE("expense"),
    ASSETS("assets"),
    LIABILITY("liability");

    private String category;

    Category(String category) {
        this.category = category;
    }

    @JsonValue
    public String getCategory(){
        return category;
    }
}
