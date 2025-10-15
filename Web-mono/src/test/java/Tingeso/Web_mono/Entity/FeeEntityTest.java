package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

class FeeEntityTest {
    @Test
    void testBuilderAndGettersSetters() {
        FeeEntity fee = FeeEntity.builder().id(1L).repoFee(10).rentalFee(20).lateFee(30).maintenanceFee(40).build();
        fee.setTools(Collections.emptyList());
        assertEquals(1L, fee.getId());
        assertEquals(10, fee.getRepoFee());
        assertEquals(20, fee.getRentalFee());
        assertEquals(30, fee.getLateFee());
        assertEquals(40, fee.getMaintenanceFee());
        assertNotNull(fee.getTools());
    }
}

