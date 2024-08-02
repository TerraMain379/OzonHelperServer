package me.terramain.ozonhelperserver;

import com.google.gson.Gson;
import me.terramain.ozonhelperserver.config.Config;
import me.terramain.textexecuter.TextHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


@SpringBootApplication
public class Main {
    public static Config config;
    public static void main(String[] args) {
        main();
        SpringApplication.run(Main.class);
    }
    public static void main() {
        Gson gson = new Gson();
        try {
            config = gson.fromJson(new FileReader(new File("config.json")),Config.class);
        } catch (FileNotFoundException e) {throw new RuntimeException(e);}
        System.out.println(config.api.client_id);
        //System.out.println(TextHelper.readFile(config.api.api_key.file));
    }
}
