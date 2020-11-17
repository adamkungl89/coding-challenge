package com.adamspokes.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AccountingEndpoint {
    
    @RequestMapping("/")
    public String index() {
        return "Testing, testing";
    }
    
}
