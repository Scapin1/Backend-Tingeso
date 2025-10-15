package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Entity.LoanEntity;
import Tingeso.Web_mono.Service.LoanService;
import Tingeso.Web_mono.Controller.models.ClientWithMostLoansDTO;
import Tingeso.Web_mono.Controller.models.ClientWithMostOverduesDTO;
import Tingeso.Web_mono.Controller.models.ToolWithMostOverduesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanControllerTest {
    @Mock
    private LoanService loanService;
    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGetAllLoans() {
        when(loanService.getAllLoans()).thenReturn(Collections.emptyList());
        assertNotNull(loanController.getAllLoans());
        verify(loanService).getAllLoans();
    }

    @Test
    void testAddLoan() {
        LoanEntity loan = new LoanEntity();
        when(loanService.addLoan(any(LoanEntity.class), anyString())).thenReturn(loan);
        assertEquals(loan, loanController.addLoan(loan, "user"));
    }

    @Test
    void testReturnLoan() {
        LoanEntity loan = new LoanEntity();
        when(loanService.returnLoan(1L, false, "user")).thenReturn(loan);
        assertEquals(loan, loanController.returnLoan(1L, false, "user"));
    }

    @Test
    void testGetClientWithMostLoans() {
        List<ClientWithMostLoansDTO> list = Collections.singletonList(new ClientWithMostLoansDTO());
        when(loanService.getClientWithMostLoans()).thenReturn(list);
        assertEquals(list, loanController.getClientWithMostLoans());
        verify(loanService).getClientWithMostLoans();
    }

    @Test
    void testGetClientsWithMostOverdues() {
        List<ClientWithMostOverduesDTO> list = Collections.singletonList(new ClientWithMostOverduesDTO());
        when(loanService.getClientsWithMostOverdues()).thenReturn(list);
        assertEquals(list, loanController.getClientsWithMostOverdues());
        verify(loanService).getClientsWithMostOverdues();
    }

    @Test
    void testGetToolWithMostOverdues() {
        ToolWithMostOverduesDTO dto = new ToolWithMostOverduesDTO();
        when(loanService.getToolWithMostOverdues()).thenReturn(dto);
        assertEquals(dto, loanController.getToolWithMostOverdues());
        verify(loanService).getToolWithMostOverdues();
    }
}
