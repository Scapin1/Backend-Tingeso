package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientStateTest {
    @Test
    void testValuesAndValueOf() {
        assertEquals(ClientState.ACTIVE, ClientState.valueOf("ACTIVE"));
        assertEquals(ClientState.RESTRICTED, ClientState.valueOf("RESTRICTED"));
        assertEquals(2, ClientState.values().length);
    }
}

