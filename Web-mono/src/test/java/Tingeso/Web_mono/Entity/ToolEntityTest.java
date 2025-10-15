package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToolEntityTest {
    @Test
    void testBuilderAndGettersSetters() {
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").build();
        tool.setFee(new FeeEntity());
        assertEquals(2L, tool.getId());
        assertEquals("Taladro", tool.getName());
        assertNotNull(tool.getFee());
    }
}

