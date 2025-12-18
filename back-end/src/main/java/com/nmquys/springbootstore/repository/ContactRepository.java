package com.nmquys.springbootstore.repository;

import com.nmquys.springbootstore.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
