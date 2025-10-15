package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoanEntityTest {
    @Test
    void testBuilderAndGettersSetters() {
        LoanEntity loan = LoanEntity.builder().id(3L).build();
        loan.setStatus(LoanState.NORMAL);
        assertEquals(3L, loan.getId());
        assertEquals(LoanState.NORMAL, loan.getStatus());
    }
}

