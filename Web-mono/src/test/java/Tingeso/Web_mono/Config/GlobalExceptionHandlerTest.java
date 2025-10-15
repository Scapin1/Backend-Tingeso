package Tingeso.Web_mono.Config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    @Test
    void testHandleResponseStatusException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error");
        ResponseEntity<Map<String, String>> response = handler.handleResponseStatusException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsValue("Error"));
    }
}
