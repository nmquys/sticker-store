package com.nmquys.springbootstore.controller;

import com.nmquys.springbootstore.dto.ContactRequestDto;
import com.nmquys.springbootstore.service.IContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/contacts")
@RequiredArgsConstructor    //tu dong tao cac constructor cho cac final
public class ContactController {

    private final IContactService iContactService;

    @PostMapping
    public ResponseEntity<String> saveContact( @Valid @RequestBody ContactRequestDto contactRequestDto)
    {
        //ko pass validation cua entities thi khong the chay logic
        iContactService.saveContact(contactRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Request processed successfully");
    }

}