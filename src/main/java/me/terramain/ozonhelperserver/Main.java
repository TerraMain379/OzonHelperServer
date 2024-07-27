package me.terramain.ozonhelperserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

//@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //SpringApplication.run(Main.class);
        try {
            OzonApi.getStocks("21100-1701112-00");
        } catch (IOException e) {throw new RuntimeException(e);}
    }
}
