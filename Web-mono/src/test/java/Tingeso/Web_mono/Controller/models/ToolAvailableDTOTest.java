package Tingeso.Web_mono.Controller.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToolAvailableDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        ToolAvailableDTO dto = new ToolAvailableDTO("Taladro", "Electrica", 5L, 100, 50, 30, 10);
        assertEquals("Taladro", dto.getName());
        assertEquals("Electrica", dto.getCategory());
        assertEquals(5L, dto.getStock());
        assertEquals(100, dto.getRepoFee());
        assertEquals(50, dto.getMaintenanceFee());
        assertEquals(30, dto.getRentalFee());
        assertEquals(10, dto.getLateFee());
        dto.setName("Martillo");
        dto.setCategory("Manual");
        dto.setStock(10L);
        dto.setRepoFee(200);
        dto.setMaintenanceFee(60);
        dto.setRentalFee(40);
        dto.setLateFee(20);
        assertEquals("Martillo", dto.getName());
        assertEquals("Manual", dto.getCategory());
        assertEquals(10L, dto.getStock());
        assertEquals(200, dto.getRepoFee());
        assertEquals(60, dto.getMaintenanceFee());
        assertEquals(40, dto.getRentalFee());
        assertEquals(20, dto.getLateFee());
    }
    @Test
    void testNoArgsConstructor() {
        ToolAvailableDTO dto = new ToolAvailableDTO();
        assertNull(dto.getName());
        assertNull(dto.getCategory());
        assertNull(dto.getStock());
        assertEquals(0, dto.getRepoFee());
        assertEquals(0, dto.getMaintenanceFee());
        assertEquals(0, dto.getRentalFee());
        assertEquals(0, dto.getLateFee());
    }
    @Test
    void testBuilder() {
        ToolAvailableDTO dto = ToolAvailableDTO.builder().name("Destornillador").category("Manual").stock(3L).repoFee(10).maintenanceFee(5).rentalFee(2).lateFee(1).build();
        assertEquals("Destornillador", dto.getName());
        assertEquals("Manual", dto.getCategory());
        assertEquals(3L, dto.getStock());
        assertEquals(10, dto.getRepoFee());
        assertEquals(5, dto.getMaintenanceFee());
        assertEquals(2, dto.getRentalFee());
        assertEquals(1, dto.getLateFee());
    }
}
