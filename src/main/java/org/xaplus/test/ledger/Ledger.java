package org.xaplus.test.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.xaplus")
public class Ledger {

    public static void main(String[] args) {
        SpringApplication.run(Ledger.class, args);
    }
}
