package com.assignment.bitespeed.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDtoOutput
{
    private int primaryContactId;
    private List<String> emails;

    private List<String> phoneNumbers;

    private List<String> secondaryContactIds;
}
