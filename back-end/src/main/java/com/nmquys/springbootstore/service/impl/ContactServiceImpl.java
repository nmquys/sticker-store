package com.nmquys.springbootstore.service.impl;


import com.nmquys.springbootstore.dto.ContactRequestDto;
import com.nmquys.springbootstore.entity.Contact;
import com.nmquys.springbootstore.repository.ContactRepository;
import com.nmquys.springbootstore.service.IContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService
{

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto)
    {
        Contact contact = transformToEntity(contactRequestDto);
        contactRepository.save(contact);
        return true;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto)
    {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);   //transform all the same name field from product to contactDTO
        return contact;
    }
}
