package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {

    private Transfer transfer;

    @BeforeEach
    public void Setup() {
        transfer = new Transfer();
    }

    @Test
    public void createTransferShouldNotBeNull() {
        assertNotNull(transfer);
    }

}
