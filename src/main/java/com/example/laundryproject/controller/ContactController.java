package com.example.laundryproject.controller;


import com.example.laundryproject.model.Contact;
import com.example.laundryproject.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@Validated
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping("/submit")
    public ResponseEntity<String> submitContactForm(@Valid @RequestBody Contact contact) {
        contactRepository.save(contact);
        return new ResponseEntity<>("Contact form submitted successfully!", HttpStatus.CREATED);
    }
}
