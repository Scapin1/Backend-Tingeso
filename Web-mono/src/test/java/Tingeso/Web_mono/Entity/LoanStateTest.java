package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoanStateTest {
    @Test
    void testValuesAndValueOf() {
        assertEquals(LoanState.IN_REPAIR, LoanState.valueOf("IN_REPAIR"));
        assertEquals(LoanState.NORMAL, LoanState.valueOf("NORMAL"));
        assertEquals(LoanState.FINISHED, LoanState.valueOf("FINISHED"));
        assertEquals(LoanState.OVERDUE, LoanState.valueOf("OVERDUE"));
        assertEquals(LoanState.LATE_RETURN, LoanState.valueOf("LATE_RETURN"));
        assertEquals(5, LoanState.values().length);
    }
}

