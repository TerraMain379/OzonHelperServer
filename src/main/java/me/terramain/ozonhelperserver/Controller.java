package me.terramain.ozonhelperserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping
    public String openTest(){
        return "{server:\"open\"}";
    }

    @GetMapping("/stocks/{articul}")
    public String getStocks(@PathVariable String articul){
        return articul;
    }
}
