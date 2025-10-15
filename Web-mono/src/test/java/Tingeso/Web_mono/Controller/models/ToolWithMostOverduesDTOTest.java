package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToolWithMostOverduesDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        ToolWithMostOverduesDTO dto = new ToolWithMostOverduesDTO("Taladro", 5L);
        assertEquals("Taladro", dto.getToolName());
        assertEquals(5L, dto.getOverdueCount());
        dto.setToolName("Martillo");
        dto.setOverdueCount(10L);
        assertEquals("Martillo", dto.getToolName());
        assertEquals(10L, dto.getOverdueCount());
    }
    @Test
    void testNoArgsConstructor() {
        ToolWithMostOverduesDTO dto = new ToolWithMostOverduesDTO();
        assertNull(dto.getToolName());
        assertNull(dto.getOverdueCount());
    }
}
