package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.ClientWithMostLoansDTO;
import Tingeso.Web_mono.Controller.models.ClientWithMostOverduesDTO;
import Tingeso.Web_mono.Controller.models.LoanDTO;
import Tingeso.Web_mono.Controller.models.ToolWithMostOverduesDTO;
import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ToolRepository toolRepository;
    @Mock
    private ToolService toolService;
    @Mock
    private ClientService clientService;
    @Mock
    private KardexRepository kardexRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLoans() {
        LoanDTO dto = new LoanDTO();
        doNothing().when(loanRepository).markOverdueLoans();
        when(loanRepository.findAllLoan()).thenReturn(Collections.singletonList(dto));
        List<LoanDTO> result = loanService.getAllLoans();
        assertEquals(1, result.size());
        verify(loanRepository).markOverdueLoans();
        verify(loanRepository).findAllLoan();
    }

    @Test
    void testAddLoan_StockError() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setClientState(ClientState.ACTIVE);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(0);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_ClientRestricted() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setClientState(ClientState.RESTRICTED);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2)); // Fecha de devolución válida
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(true);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_ReturnDateBeforeLoanDate() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        client.setId(1L);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().minusDays(1));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_MaxLoansReached() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        client.setId(1L);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(5L);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_AlreadyHasTool() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setClientState(ClientState.ACTIVE);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(0L);
        when(loanRepository.existsByToolAndClient("Taladro", client)).thenReturn(true);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_LoanPeriodTooShort() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        client.setId(1L);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now());
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(0L);
        when(loanRepository.existsByToolAndClient("Taladro", client)).thenReturn(false);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_ClientHasDebt() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setDebt(100);
        client.setClientState(ClientState.ACTIVE);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(0L);
        when(loanRepository.existsByToolAndClient("Taladro", client)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(0L);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.LOCKED, ex.getStatusCode());
    }

    @Test
    void testAddLoan_Success() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setDebt(0);
        client.setClientState(ClientState.ACTIVE);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(10);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(0L);
        when(loanRepository.existsByToolAndClient("Taladro", client)).thenReturn(false);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        LoanEntity result = loanService.addLoan(loan, "user");
        assertNotNull(result);
        verify(toolRepository).save(any(ToolEntity.class));
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
    }

    @Test
    void testReturnLoan_AlreadyReturned() {
        LoanEntity loan = new LoanEntity();
        loan.setStatus(LoanState.FINISHED);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.returnLoan(1L, false, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testReturnLoan_LateWithDamage() {
        LoanEntity loan = new LoanEntity();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().minusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        LoanEntity result = loanService.returnLoan(1L, true, "user");
        assertNotNull(result);
        verify(toolService).sentMaintenance(eq(tool.getId()), anyString());
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
    }

    @Test
    void testReturnLoan_LateWithDamage_NoNullClientState() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().minusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        LoanEntity result = loanService.returnLoan(1L, true, "user");
        assertNotNull(result);
        verify(toolService).sentMaintenance(eq(tool.getId()), anyString());
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
    }

    @Test
    void testReturnLoan_LateWithDamage_NullClientState() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        ClientEntity client = new ClientEntity();
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().minusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.returnLoan(1L, true, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Client state cannot be null"));
    }

    @Test
    void testReturnLoan_LateNoDamage() {
        LoanEntity loan = new LoanEntity();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().minusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        LoanEntity result = loanService.returnLoan(1L, false, "user");
        assertNotNull(result);
        verify(toolRepository).save(any(ToolEntity.class));
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
    }

    @Test
    void testReturnLoan_OnTime() {
        LoanEntity loan = new LoanEntity();
        ClientEntity client = new ClientEntity();
        client.setClientState(ClientState.ACTIVE);
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        LoanEntity result = loanService.returnLoan(1L, false, "user");
        assertNotNull(result);
        verify(toolRepository).save(any(ToolEntity.class));
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
    }

    @Test
    void testGetClientWithMostLoans() {
        when(loanRepository.findClientsWithMostLoans()).thenReturn(Collections.emptyList());
        assertNotNull(loanService.getClientWithMostLoans());
        verify(loanRepository).findClientsWithMostLoans();
    }

    @Test
    void testGetClientsWithMostOverdues() {
        when(loanRepository.findClientsWithMostOverdues()).thenReturn(Collections.emptyList());
        assertNotNull(loanService.getClientsWithMostOverdues());
        verify(loanRepository).findClientsWithMostOverdues();
    }

    @Test
    void testGetToolWithMostOverdues() {
        when(loanRepository.findToolWithMostOverdues()).thenReturn(null);
        assertNull(loanService.getToolWithMostOverdues());
        verify(loanRepository).findToolWithMostOverdues();
    }

    @Test
    void testAddLoan_NullLoan() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(null, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testAddLoan_NullClient() {
        LoanEntity loan = new LoanEntity();
        loan.setToolLoaned(new ToolEntity());
        loan.setReturnDate(LocalDate.now());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testAddLoan_NullTool() {
        LoanEntity loan = new LoanEntity();
        loan.setClient(new ClientEntity());
        loan.setReturnDate(LocalDate.now());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testAddLoan_NullReturnDate() {
        LoanEntity loan = new LoanEntity();
        loan.setClient(new ClientEntity());
        loan.setToolLoaned(new ToolEntity());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testAddLoan_ToolNotFound() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setClientState(ClientState.ACTIVE);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(null);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testAddLoan_ClientNotFound() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().name("Taladro").build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setClientState(ClientState.ACTIVE);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(null);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.addLoan(loan, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testReturnLoan_LoanNull() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> loanService.returnLoan(1L, false, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testReturnLoan_ClientWithDebtAndRestricted() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        ClientEntity client = new ClientEntity();
        client.setDebt(100);
        client.setClientState(ClientState.RESTRICTED);
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        LoanEntity result = loanService.returnLoan(1L, false, "user");
        assertNotNull(result);
        verify(clientRepository).save(client);
        assertEquals(ClientState.ACTIVE, client.getClientState());
        assertTrue(client.getDebt() > 0);
    }

    @Test
    void testReturnLoan_ClientWithNoDebtAndActive() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().id(2L).name("Taladro").fee(new FeeEntity()).build();
        ClientEntity client = new ClientEntity();
        client.setDebt(0);
        client.setClientState(ClientState.ACTIVE);
        loan.setStatus(LoanState.NORMAL);
        loan.setReturnDate(LocalDate.now().plusDays(2));
        loan.setToolLoaned(tool);
        loan.setClient(client);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        LoanEntity result = loanService.returnLoan(1L, false, "user");
        assertNotNull(result);
        verify(clientRepository).save(client);
        assertEquals(ClientState.ACTIVE, client.getClientState());
        assertEquals(0, client.getDebt());
    }

    @Test
    void testGetAllLoans_CallsMarkOverdueAndReturnsList() {
        LoanDTO dto = new LoanDTO();
        doNothing().when(loanRepository).markOverdueLoans();
        when(loanRepository.findAllLoan()).thenReturn(Collections.singletonList(dto));
        List<LoanDTO> result = loanService.getAllLoans();
        assertEquals(1, result.size());
        verify(loanRepository).markOverdueLoans();
        verify(loanRepository).findAllLoan();
    }

    @Test
    void testGetClientWithMostLoans_ReturnsNonEmptyList() {
        List<ClientWithMostLoansDTO> list = Collections.singletonList(new ClientWithMostLoansDTO());
        when(loanRepository.findClientsWithMostLoans()).thenReturn(list);
        List<ClientWithMostLoansDTO> result = loanService.getClientWithMostLoans();
        assertEquals(1, result.size());
        verify(loanRepository).findClientsWithMostLoans();
    }

    @Test
    void testGetClientsWithMostOverdues_ReturnsNonEmptyList() {
        List<ClientWithMostOverduesDTO> list = Collections.singletonList(new ClientWithMostOverduesDTO());
        when(loanRepository.findClientsWithMostOverdues()).thenReturn(list);
        List<ClientWithMostOverduesDTO> result = loanService.getClientsWithMostOverdues();
        assertEquals(1, result.size());
        verify(loanRepository).findClientsWithMostOverdues();
    }

    @Test
    void testGetToolWithMostOverdues_ReturnsDTO() {
        ToolWithMostOverduesDTO dto = new ToolWithMostOverduesDTO();
        when(loanRepository.findToolWithMostOverdues()).thenReturn(dto);
        ToolWithMostOverduesDTO result = loanService.getToolWithMostOverdues();
        assertNotNull(result);
        verify(loanRepository).findToolWithMostOverdues();
    }

    @Test
    void testAddLoan_SuccessfulLoan() {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = ToolEntity.builder().id(10L).name("Taladro").fee(new FeeEntity()).build();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setClientState(ClientState.ACTIVE);
        client.setDebt(0);
        loan.setToolLoaned(tool);
        loan.setClient(client);
        loan.setReturnDate(LocalDate.now().plusDays(3));
        when(toolRepository.findTopByNameAndState("Taladro", ToolStateType.AVAILABLE)).thenReturn(tool);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(toolRepository.getStock("Taladro")).thenReturn(5);
        when(loanRepository.existsOverdueLoanByClientId(1L)).thenReturn(false);
        when(loanRepository.countByClientIdAndStatus(1L, LoanState.NORMAL)).thenReturn(0L);
        when(loanRepository.existsByToolAndClient("Taladro", client)).thenReturn(false);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        LoanEntity result = loanService.addLoan(loan, "usuarioTest");
        assertNotNull(result);
        verify(toolRepository).save(any(ToolEntity.class));
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
    }
}
