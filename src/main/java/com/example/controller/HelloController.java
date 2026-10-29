package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 //Это чисто написал когда начинал тупо написано и все =)
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayhello(){
        return "Привет! Первый шаг сделан!";
    }
}
