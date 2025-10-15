package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KardexMovementTypeTest {
    @Test
    void testValuesAndValueOf() {
        assertEquals(KardexMovementType.INCOME, KardexMovementType.valueOf("INCOME"));
        assertEquals(KardexMovementType.LOAN, KardexMovementType.valueOf("LOAN"));
        assertEquals(KardexMovementType.RETURN, KardexMovementType.valueOf("RETURN"));
        assertEquals(KardexMovementType.WRITE_OFF, KardexMovementType.valueOf("WRITE_OFF"));
        assertEquals(KardexMovementType.REPAIR, KardexMovementType.valueOf("REPAIR"));
        assertEquals(5, KardexMovementType.values().length);
    }
}

