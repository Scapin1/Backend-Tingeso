package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToolStateTypeTest {
    @Test
    void testValuesAndValueOf() {
        assertEquals(ToolStateType.AVAILABLE, ToolStateType.valueOf("AVAILABLE"));
        assertEquals(ToolStateType.LOANED, ToolStateType.valueOf("LOANED"));
        assertEquals(ToolStateType.IN_REPAIR, ToolStateType.valueOf("IN_REPAIR"));
        assertEquals(ToolStateType.WRITTEN_OFF, ToolStateType.valueOf("WRITTEN_OFF"));
        assertEquals(4, ToolStateType.values().length);
    }
}

