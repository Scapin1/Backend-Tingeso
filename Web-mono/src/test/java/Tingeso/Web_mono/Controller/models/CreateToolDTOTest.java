package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreateToolDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        CreateToolDTO dto = new CreateToolDTO("Taladro", 2, 100, "Electrica");
        assertEquals("Taladro", dto.name);
        assertEquals(2, dto.quantity);
        assertEquals(100, dto.repoFee);
        assertEquals("Electrica", dto.category);
        dto.name = "Martillo";
        dto.quantity = 5;
        dto.repoFee = 200;
        dto.category = "Manual";
        assertEquals("Martillo", dto.name);
        assertEquals(5, dto.quantity);
        assertEquals(200, dto.repoFee);
        assertEquals("Manual", dto.category);
    }
    @Test
    void testNoArgsConstructor() {
        CreateToolDTO dto = new CreateToolDTO();
        assertNull(dto.name);
        assertEquals(0, dto.quantity);
        assertEquals(0, dto.repoFee);
        assertNull(dto.category);
    }
    @Test
    void testBuilder() {
        CreateToolDTO dto = CreateToolDTO.builder().name("Destornillador").quantity(3).repoFee(50).category("Manual").build();
        assertEquals("Destornillador", dto.name);
        assertEquals(3, dto.quantity);
        assertEquals(50, dto.repoFee);
        assertEquals("Manual", dto.category);
    }
}
