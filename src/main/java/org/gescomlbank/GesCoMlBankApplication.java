package org.gescomlbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GesCoMlBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(GesCoMlBankApplication.class, args);
    }

}
