package com.strix_invoice.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/hello")
    public String greet() {
        return "Welcome to Strix App";
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name){
        return "Hello "+name;
    }
}