package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void testValuesAndValueOf() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.EMPLOYEE, Role.valueOf("EMPLOYEE"));
        assertEquals(2, Role.values().length);
    }
}

