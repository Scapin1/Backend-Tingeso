package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class KardexEntityTest {
    @Test
    void testBuilderAndGettersSetters() {
        KardexEntity kardex = KardexEntity.builder()
            .id(1L)
            .toolId(2L)
            .toolName("Martillo")
            .userName("Pedro")
            .quantity(5)
            .movementDate(LocalDateTime.now())
            .type(KardexMovementType.INCOME)
            .build();
        assertEquals(1L, kardex.getId());
        assertEquals(2L, kardex.getToolId());
        assertEquals("Martillo", kardex.getToolName());
        assertEquals("Pedro", kardex.getUserName());
        assertEquals(5, kardex.getQuantity());
        assertNotNull(kardex.getMovementDate());
        assertEquals(KardexMovementType.INCOME, kardex.getType());
    }
}

