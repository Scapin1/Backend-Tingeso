package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Entity.ClientEntity;
import Tingeso.Web_mono.Service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {
    @Mock
    private ClientService clientService;
    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClients() {
        when(clientService.getAll()).thenReturn(Collections.emptyList());
        assertNotNull(clientController.getClients());
        verify(clientService).getAll();
    }

    @Test
    void testGetClients_EmptyList() {
        when(clientService.getAll()).thenReturn(Collections.emptyList());
        assertTrue(clientController.getClients().isEmpty());
    }

    @Test
    void testAddClient() {
        ClientEntity client = new ClientEntity();
        when(clientService.save(client)).thenReturn(client);
        assertEquals(client, clientController.addClient(client));
        verify(clientService).save(client);
    }

    @Test
    void testAddClient_Null() {
        when(clientService.save(null)).thenReturn(null);
        assertNull(clientController.addClient(null));
    }

    @Test
    void testChangeState() {
        ClientEntity client = new ClientEntity();
        when(clientService.changeState(1L)).thenReturn(client);
        assertEquals(client, clientController.changeState(1L));
        verify(clientService).changeState(1L);
    }

    @Test
    void testUpdateClient() {
        ClientEntity client = new ClientEntity();
        when(clientService.updateClient(client)).thenReturn(client);
        assertEquals(client, clientController.updateClient(client));
        verify(clientService).updateClient(client);
    }

    @Test
    void testUpdateClient_Null() {
        when(clientService.updateClient(null)).thenReturn(null);
        assertNull(clientController.updateClient(null));
    }

    @Test
    void testChangeState_NotFound() {
        when(clientService.changeState(99L)).thenReturn(null);
        assertNull(clientController.changeState(99L));
        verify(clientService).changeState(99L);
    }
}
