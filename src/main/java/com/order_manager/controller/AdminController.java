package com.order_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public ResponseEntity<String> admin() {
        System.out.println("Say hello");
        return ResponseEntity.ok("Hello, world!");
    }
}