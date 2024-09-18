package me.terramain.ozonhelperserver.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public static String test(){
        return "OK";
    }
}
