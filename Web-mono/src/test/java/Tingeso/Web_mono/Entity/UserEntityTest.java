package Tingeso.Web_mono.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    @Test
    void testBuilderAndGettersSetters() {
        UserEntity user = UserEntity.builder().id(1L).email("test@mail.com").password("1234").firstName("Ana").lastName("Perez").role(Role.ADMIN).build();
        assertEquals(1L, user.getId());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("1234", user.getPassword());
        assertEquals("Ana", user.getFirstName());
        assertEquals("Perez", user.getLastName());
        assertEquals(Role.ADMIN, user.getRole());
    }
}

