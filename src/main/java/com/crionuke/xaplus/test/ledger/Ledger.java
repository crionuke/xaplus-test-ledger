package com.crionuke.xaplus.test.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.crionuke")
public class Ledger {

    public static void main(String[] args) {
        SpringApplication.run(Ledger.class, args);
    }
}
