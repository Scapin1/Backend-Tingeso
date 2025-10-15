package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        assertNotNull(userController.getUsers());
        verify(userService).getAllUsers();
    }

    @Test
    void testGetUsers_EmptyList() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        assertTrue(userController.getUsers().isEmpty());
    }
}
