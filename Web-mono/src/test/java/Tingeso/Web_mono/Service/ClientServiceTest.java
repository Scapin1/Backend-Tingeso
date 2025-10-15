package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(clientService.getAll());
        verify(clientRepository).findAll();
    }

    @Test
    void testSave() {
        ClientEntity client = new ClientEntity();
        when(clientRepository.save(client)).thenReturn(client);
        assertEquals(client, clientService.save(client));
        verify(clientRepository).save(client);
    }

    @Test
    void testChangeState() {
        ClientEntity client = new ClientEntity();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        assertEquals(client, clientService.changeState(1L));
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(client);
    }

    @Test
    void testUpdateClient() {
        ClientEntity client = new ClientEntity();
        when(clientRepository.save(client)).thenReturn(client);
        assertEquals(client, clientService.updateClient(client));
        verify(clientRepository).save(client);
    }

    @Test
    void testChangeState_ClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clientService.changeState(1L));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testChangeState_FromRestrictedToActive() {
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.RESTRICTED);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        ClientEntity result = clientService.changeState(1L);
        assertEquals(ClientState.ACTIVE, result.getClientState());
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(client);
    }
}
