package com.assignment.bitespeed.Service;

import com.assignment.bitespeed.Dto.ContactDtoInput;
import com.assignment.bitespeed.Dto.ContactDtoOutput;
import com.assignment.bitespeed.Entity.Contact;
import com.assignment.bitespeed.Repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ContactService
{
    private final ContactRepository contactRepository;
    public ContactDtoOutput insertContact(ContactDtoInput contactDtoInput)
    {
        ContactDtoOutput contactDtoOutput=new ContactDtoOutput();

        Contact contact1=contactRepository.findByEmail(contactDtoInput.getEmail());
        Contact contact2=contactRepository.findByPhoneNumber(contactDtoInput.getPhoneNumber());
        int id=0;
        if(contact1==null && contact2==null)
        {
            System.out.println("NEW RECORD TO BE INSERTED");
            id=createPrimaryContact(contactDtoInput);


        }
        else if(contact1 !=null && contact2 !=null)
        {
            System.out.println("UPDATING CONTACT DETAILS WHEN BOTH ARE PRIMARY RECORDS");
            id=updatePrimaryToSecondary(contactDtoInput);
        }
        else if (contact1!=null && contact2==null)
        {
            System.out.println("WHEN EMAIL IS REPEATED BUT PHONE NUMBER IS NEW");
            id=creatSecondaryContact(contactDtoInput,contact1);

        }
        else if (contact2!=null && contact1==null)
        {
            System.out.println("WHEN PHONE NUMBER IS SAME BUT EMAIL IS NEW");
            id=creatSecondaryContact(contactDtoInput,contact2);
        }


        return mapToOutput(id);
    }

    public int createPrimaryContact(ContactDtoInput contactDtoInput)
    {
        Contact contact=new Contact();
        contact.setEmail(contactDtoInput.getEmail());
        contact.setPhoneNumber(contactDtoInput.getPhoneNumber());
        contact.setLinkPrecedence("Primary");
        contact.setCreatedAt(new Date());
        contact.setUpdatedAt(new Date());
        contactRepository.save(contact);
        return contact.getId();

    }


    public int updatePrimaryToSecondary(ContactDtoInput contactDtoInput)
    {
        Contact contact3=contactRepository.findByEmailAndLinkPrecedence(contactDtoInput.getEmail(),"Primary");
        Contact contact4=contactRepository.findByPhoneNumberAndLinkPrecedence(contactDtoInput.getPhoneNumber(),"Primary");
        int id=-1;
        if(contact3!=null && contact4!=null)
        {
            if(contact3.getId()==contact4.getId())
            {
                contact3.setUpdatedAt(new Date());
                contactRepository.save(contact3);
                System.out.println("SAME CONTACT REPEATED");
                return contact3.getId();
            }
            else
            {
                if (contact3.getCreatedAt().before(contact4.getCreatedAt()))
                {
                    contact4.setLinkPrecedence("Secondary");
                    contact4.setUpdatedAt(new Date());
                    contact4.setLinkedId(contact3.getId());
                    contactRepository.save(contact4);
                    contactRepository.update(contact3.getId(),contact4.getId());
                    return contact3.getId();
                }
                else
                {
                    contact3.setLinkPrecedence("Secondary");
                    contact3.setUpdatedAt(new Date());
                    contact3.setLinkedId(contact4.getId());
                    contactRepository.save(contact3);
                    contactRepository.update(contact4.getId(),contact3.getId());
                    return contact4.getId();
                }
            }
        }
        if(id==-1)
        {
            System.out.println("SAME CONTACT IS REPEATED");
        }
        return id;
    }

    public int creatSecondaryContact(ContactDtoInput contactDtoInput, Contact contact)
    {
        Contact newContact=new Contact();
        newContact.setEmail(contactDtoInput.getEmail());
        newContact.setPhoneNumber(contactDtoInput.getPhoneNumber());
        newContact.setLinkPrecedence("Secondary");
        if(contact.getLinkPrecedence().equals("Primary"))
        {
            newContact.setLinkedId(contact.getId());
        }
        else
        {
            newContact.setLinkedId(contact.getLinkedId());
        }
        newContact.setCreatedAt(new Date());
        newContact.setUpdatedAt(new Date());
        contactRepository.save(newContact);
        return newContact.getLinkedId();
    }

    public ContactDtoOutput mapToOutput(int id)
    {
        ContactDtoOutput contactDtoOutput=new ContactDtoOutput();
        contactDtoOutput.setPrimaryContactId(id);
        contactDtoOutput.setEmails(contactRepository.findEmailByLinkedId(id));
        contactDtoOutput.setSecondaryContactIds(contactRepository.findSecId(id));
        contactDtoOutput.setPhoneNumbers(contactRepository.findPhoneNumbers(id));
        return contactDtoOutput;
    }

    public void selectAll()
    {
        List<Contact> contacts=contactRepository.findAll();
        System.out.println(contacts);
    }
}
