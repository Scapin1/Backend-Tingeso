package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientEntityTest {
    @Test
    void testBuilderAndGettersSetters() {
        ClientEntity client = ClientEntity.builder().id(1L).firstName("Juan").debt(100).build();
        client.setClientState(ClientState.ACTIVE);
        assertEquals(1L, client.getId());
        assertEquals("Juan", client.getFirstName());
        assertEquals(100, client.getDebt());
        assertEquals(ClientState.ACTIVE, client.getClientState());
    }
}

