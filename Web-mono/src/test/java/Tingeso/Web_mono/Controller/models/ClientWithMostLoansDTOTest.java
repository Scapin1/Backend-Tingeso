package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientWithMostLoansDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        ClientWithMostLoansDTO dto = new ClientWithMostLoansDTO("12345678-9", 5L);
        assertEquals("12345678-9", dto.getClientRut());
        assertEquals(5L, dto.getLoanCount());
        dto.setClientRut("98765432-1");
        dto.setLoanCount(10L);
        assertEquals("98765432-1", dto.getClientRut());
        assertEquals(10L, dto.getLoanCount());
    }
    @Test
    void testNoArgsConstructor() {
        ClientWithMostLoansDTO dto = new ClientWithMostLoansDTO();
        assertNull(dto.getClientRut());
        assertNull(dto.getLoanCount());
    }
}
