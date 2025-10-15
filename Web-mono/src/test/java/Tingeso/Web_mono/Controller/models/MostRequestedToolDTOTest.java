package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MostRequestedToolDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        MostRequestedToolDTO dto = new MostRequestedToolDTO("Taladro", 5L);
        assertEquals("Taladro", dto.getToolName());
        assertEquals(5L, dto.getRequestCount());
        dto.setToolName("Martillo");
        dto.setRequestCount(10L);
        assertEquals("Martillo", dto.getToolName());
        assertEquals(10L, dto.getRequestCount());
    }
    @Test
    void testNoArgsConstructor() {
        MostRequestedToolDTO dto = new MostRequestedToolDTO();
        assertNull(dto.getToolName());
        assertNull(dto.getRequestCount());
    }
    @Test
    void testBuilder() {
        MostRequestedToolDTO dto = MostRequestedToolDTO.builder().toolName("Destornillador").requestCount(3L).build();
        assertEquals("Destornillador", dto.getToolName());
        assertEquals(3L, dto.getRequestCount());
    }
}
