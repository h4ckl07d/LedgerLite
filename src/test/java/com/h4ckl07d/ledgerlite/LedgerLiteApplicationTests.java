package com.h4ckl07d.ledgerlite;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class LedgerLiteApplicationTests {

    @Test
    void contextLoads() {
    }

}
