package com.assignment.bitespeed.Controller;

import com.assignment.bitespeed.Dto.ContactDtoInput;
import com.assignment.bitespeed.Dto.ContactDtoOutput;
import com.assignment.bitespeed.Entity.Contact;
import com.assignment.bitespeed.Service.ContactService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController
{
    private final ContactService contactService;
    @PostMapping("/identify")
    public ResponseEntity<ContactDtoOutput> createContact(@RequestBody ContactDtoInput contactDtoInput)
    {
        ContactDtoOutput contactDtoOutput=contactService.insertContact(contactDtoInput);
        if(contactDtoOutput.getPrimaryContactId()==-1)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(contactDtoOutput);
        }
        return ResponseEntity.status(HttpStatus.OK).body(contactDtoOutput);
    }

    @GetMapping
    public ResponseEntity<List<Contact>> findall()
    {
        List<Contact> contactList=contactService.selectAll();
        return ResponseEntity.status(HttpStatus.OK).body(contactList);
    }
}
