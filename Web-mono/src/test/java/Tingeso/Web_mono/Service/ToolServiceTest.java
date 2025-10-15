package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.CreateToolDTO;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToolServiceTest {
    @Mock
    private ToolRepository toolRepository;
    @Mock
    private FeeRepository feeRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private KardexRepository kardexRepository;
    @InjectMocks
    private ToolService toolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        toolService = new ToolService(toolRepository, feeRepository, loanRepository, clientRepository, clientService, kardexRepository);
    }

    @Test
    void testFindAll() {
        when(toolRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(toolService.findAll());
        verify(toolRepository).findAll();
    }

    @Test
    void testSave_Success() {
        CreateToolDTO dto = new CreateToolDTO();
        dto.setName("Taladro");
        dto.setCategory("Electrica");
        dto.setQuantity(2);
        dto.setRepoFee(100);
        when(toolRepository.getStock("Taladro")).thenReturn(0);
        when(feeRepository.save(any(FeeEntity.class))).thenReturn(new FeeEntity());
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(new ToolEntity());
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        assertDoesNotThrow(() -> toolService.save(dto, "user"));
        verify(toolRepository, times(2)).save(any(ToolEntity.class));
        verify(feeRepository).save(any(FeeEntity.class));
        verify(kardexRepository).save(any(KardexEntity.class));
    }

    @Test
    void testSave_ToolAlreadyExists() {
        CreateToolDTO dto = new CreateToolDTO();
        dto.setName("Taladro");
        when(toolRepository.getStock("Taladro")).thenReturn(1);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> toolService.save(dto, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testFindAllList() {
        when(toolRepository.findAvailableToolsGrouped()).thenReturn(List.of());
        assertNotNull(toolService.findAllList());
        verify(toolRepository).findAvailableToolsGrouped();
    }

    @Test
    void testSentMaintenance_Success() {
        ToolEntity tool = ToolEntity.builder().id(1L).name("Taladro").build();
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        ToolEntity result = toolService.sentMaintenance(1L, "user");
        assertEquals(ToolStateType.IN_REPAIR, result.getState());
        verify(toolRepository).save(tool);
        verify(kardexRepository).save(any(KardexEntity.class));
    }

    @Test
    void testSentMaintenance_ToolNotFound() {
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> toolService.sentMaintenance(1L, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testWriteOff_SuccessWithLoan() {
        ToolEntity tool = ToolEntity.builder().id(1L).name("Taladro").fee(new FeeEntity()).build();
        tool.setState(ToolStateType.AVAILABLE);
        LoanEntity loan = new LoanEntity();
        loan.setClient(new ClientEntity());
        loan.setLateStatus(false);
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(loanRepository.findByToolLoaned_Id(1L)).thenReturn(loan);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(new ClientEntity());
        ToolEntity result = toolService.writeOff(1L, "user");
        assertEquals(ToolStateType.WRITTEN_OFF, result.getState());
        verify(toolRepository).save(tool);
        verify(kardexRepository).save(any(KardexEntity.class));
        verify(loanRepository).save(loan);
        verify(clientRepository).save(any(ClientEntity.class));
    }

    @Test
    void testWriteOff_SuccessWithoutLoan() {
        ToolEntity tool = ToolEntity.builder().id(1L).name("Taladro").fee(new FeeEntity()).build();
        tool.setState(ToolStateType.AVAILABLE);
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(loanRepository.findByToolLoaned_Id(1L)).thenReturn(null);
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        ToolEntity result = toolService.writeOff(1L, "user");
        assertEquals(ToolStateType.WRITTEN_OFF, result.getState());
        verify(toolRepository).save(tool);
        verify(kardexRepository).save(any(KardexEntity.class));
    }

    @Test
    void testWriteOff_ToolNotFound() {
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> toolService.writeOff(1L, "user"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testGetFees_Success() {
        ToolEntity tool = ToolEntity.builder().id(1L).fee(new FeeEntity()).build();
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        FeeEntity result = toolService.getFees(1L);
        assertNotNull(result);
        assertEquals(tool.getFee(), result);
    }

    @Test
    void testGetFees_ToolNotFound() {
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> toolService.getFees(1L));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testChangeFee() {
        FeeEntity fee = new FeeEntity();
        when(feeRepository.save(fee)).thenReturn(fee);
        FeeEntity result = toolService.changeFee(fee);
        assertEquals(fee, result);
        verify(feeRepository).save(fee);
    }
}
