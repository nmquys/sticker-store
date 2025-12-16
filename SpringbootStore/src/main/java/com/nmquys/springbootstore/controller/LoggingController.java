package com.nmquys.springbootstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logging")
@Slf4j      //= private static final Logger log = LoggerFactory.getLogger(LoggingController.class);
public class LoggingController
{


    @GetMapping
    public ResponseEntity<String> testLogging()
    {
        log.trace("TRACE: This is a trace log");
        return ResponseEntity.ok().body("Logging tested succesfully");
    }
}
