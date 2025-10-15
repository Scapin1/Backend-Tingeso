package Tingeso.Web_mono.Controller.models;

import Tingeso.Web_mono.Entity.LoanState;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LoanDTOTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        LoanDTO dto = new LoanDTO(1L, LocalDate.now(), LocalDate.now().plusDays(1), LoanState.NORMAL, "Taladro", 2L, "Juan");
        assertEquals(1L, dto.getId());
        assertNotNull(dto.getLoanDate());
        assertNotNull(dto.getReturnDate());
        assertEquals(LoanState.NORMAL, dto.getStatus());
        assertEquals("Taladro", dto.getToolLoaned());
        assertEquals(2L, dto.getToolId());
        assertEquals("Juan", dto.getClient());
        dto.setId(5L);
        dto.setLoanDate(LocalDate.now().minusDays(1));
        dto.setReturnDate(LocalDate.now().plusDays(2));
        dto.setStatus(LoanState.FINISHED);
        dto.setToolLoaned("Martillo");
        dto.setToolId(10L);
        dto.setClient("Pedro");
        assertEquals(5L, dto.getId());
        assertEquals(LoanState.FINISHED, dto.getStatus());
        assertEquals("Martillo", dto.getToolLoaned());
        assertEquals(10L, dto.getToolId());
        assertEquals("Pedro", dto.getClient());
    }
    @Test
    void testNoArgsConstructor() {
        LoanDTO dto = new LoanDTO();
        assertNull(dto.getId());
        assertNull(dto.getLoanDate());
        assertNull(dto.getReturnDate());
        assertNull(dto.getStatus());
        assertNull(dto.getToolLoaned());
        assertNull(dto.getToolId());
        assertNull(dto.getClient());
    }
    @Test
    void testBuilder() {
        LoanDTO dto = LoanDTO.builder().id(1L).toolLoaned("Taladro").client("Juan").build();
        assertEquals(1L, dto.getId());
        assertEquals("Taladro", dto.getToolLoaned());
        assertEquals("Juan", dto.getClient());
    }
}
