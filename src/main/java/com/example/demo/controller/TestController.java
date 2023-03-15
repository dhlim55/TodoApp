package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("/testGetMapping")
    public String testController() {
        return "Hello World!!,임다희!!";
    }
    @GetMapping("/{id}")
    public String testControllerWithPathVariable(@PathVariable(required = false) int id){
    return "Hello WORLD ID "+id;
    }
}
