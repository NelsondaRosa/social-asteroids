package com.ndr.socialasteroids.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.presentation.payload.response.RootEntryPoint;

@RestController
@RequestMapping("/home")
public class HomeController {
    
    @GetMapping
    public ResponseEntity<?> guide()
    {
        return ResponseEntity.ok().body(new RootEntryPoint());
    }
}
