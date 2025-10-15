package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoansByMonthAndToolNameDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        LoansByMonthAndToolNameDTO dto = new LoansByMonthAndToolNameDTO("Taladro", "Enero", 5L);
        assertEquals("Taladro", dto.getToolName());
        assertEquals("Enero", dto.getMonth());
        assertEquals(5L, dto.getCount());
        dto.setToolName("Martillo");
        dto.setMonth("Febrero");
        dto.setCount(10L);
        assertEquals("Martillo", dto.getToolName());
        assertEquals("Febrero", dto.getMonth());
        assertEquals(10L, dto.getCount());
    }
    @Test
    void testNoArgsConstructor() {
        LoansByMonthAndToolNameDTO dto = new LoansByMonthAndToolNameDTO();
        assertNull(dto.getToolName());
        assertNull(dto.getMonth());
        assertNull(dto.getCount());
    }
    @Test
    void testBuilder() {
        LoansByMonthAndToolNameDTO dto = LoansByMonthAndToolNameDTO.builder().toolName("Destornillador").month("Marzo").count(3L).build();
        assertEquals("Destornillador", dto.getToolName());
        assertEquals("Marzo", dto.getMonth());
        assertEquals(3L, dto.getCount());
    }
}
