package com.assignment.bitespeed.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactDtoInput
{
    private String email;
    private String phoneNumber;
}
