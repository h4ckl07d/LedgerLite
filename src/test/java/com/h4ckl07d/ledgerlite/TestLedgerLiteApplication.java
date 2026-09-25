package com.h4ckl07d.ledgerlite;

import org.springframework.boot.SpringApplication;

public class TestLedgerLiteApplication {

    public static void main(String[] args) {
        SpringApplication.from(LedgerLiteApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
