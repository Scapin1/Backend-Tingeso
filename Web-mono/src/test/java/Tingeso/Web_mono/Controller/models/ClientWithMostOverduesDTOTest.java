package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientWithMostOverduesDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        ClientWithMostOverduesDTO dto = new ClientWithMostOverduesDTO("12345678-9", 3L);
        assertEquals("12345678-9", dto.getRut());
        assertEquals(3L, dto.getOverdueCount());
        dto.setRut("98765432-1");
        dto.setOverdueCount(7L);
        assertEquals("98765432-1", dto.getRut());
        assertEquals(7L, dto.getOverdueCount());
    }
    @Test
    void testNoArgsConstructor() {
        ClientWithMostOverduesDTO dto = new ClientWithMostOverduesDTO();
        assertNull(dto.getRut());
        assertNull(dto.getOverdueCount());
    }
}
