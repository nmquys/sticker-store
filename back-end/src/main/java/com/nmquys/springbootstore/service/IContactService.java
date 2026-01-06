package com.nmquys.springbootstore.service;

import com.nmquys.springbootstore.dto.ContactRequestDto;
import com.nmquys.springbootstore.dto.ContactResponseDto;
import com.nmquys.springbootstore.dto.ProductDto;

import java.util.List;

public interface IContactService
{

    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> getAllOpenMessages();

    void updateMessageStatus(Long contactId, String status);
}
