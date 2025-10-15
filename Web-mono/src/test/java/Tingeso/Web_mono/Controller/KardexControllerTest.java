package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Service.KardexService;
import Tingeso.Web_mono.Entity.KardexEntity;
import Tingeso.Web_mono.Controller.models.LoansByMonthAndToolNameDTO;
import Tingeso.Web_mono.Controller.models.MostRequestedToolDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KardexControllerTest {
    @Mock
    private KardexService kardexService;
    @InjectMocks
    private KardexController kardexController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllKardex() {
        when(kardexService.getAll()).thenReturn(Collections.emptyList());
        assertNotNull(kardexController.getAllKardex());
        verify(kardexService).getAll();
    }

    @Test
    void testGetAllKardex_EmptyList() {
        when(kardexService.getAll()).thenReturn(Collections.emptyList());
        assertTrue(kardexController.getAllKardex().isEmpty());
    }

    @Test
    void testGetFilteredKardex_AllNull() {
        when(kardexService.findKardex(null, null, null)).thenReturn(Collections.emptyList());
        assertNotNull(kardexController.getFilteredKardex(null, null, null));
        verify(kardexService).findKardex(null, null, null);
    }

    @Test
    void testGetFilteredKardex_WithParams() {
        LocalDateTime now = LocalDateTime.now();
        String start = now.minusDays(1).toString();
        String end = now.toString();
        when(kardexService.findKardex(1L, LocalDateTime.parse(start), LocalDateTime.parse(end))).thenReturn(Collections.emptyList());
        assertNotNull(kardexController.getFilteredKardex(start, end, 1L));
        verify(kardexService).findKardex(1L, LocalDateTime.parse(start), LocalDateTime.parse(end));
    }

    @Test
    void testGetFilteredKardex_EmptyStrings() {
        when(kardexService.findKardex(null, null, null)).thenReturn(Collections.emptyList());
        assertNotNull(kardexController.getFilteredKardex("", "", null));
        verify(kardexService).findKardex(null, null, null);
    }

    @Test
    void testGetLoansByMonthAndToolName() {
        List<LoansByMonthAndToolNameDTO> list = Collections.singletonList(new LoansByMonthAndToolNameDTO());
        when(kardexService.countLoansByMonthAndToolName()).thenReturn(list);
        assertEquals(list, kardexController.getLoansByMonthAndToolName());
        verify(kardexService).countLoansByMonthAndToolName();
    }

    @Test
    void testGetMostRequestedTool() {
        MostRequestedToolDTO dto = new MostRequestedToolDTO();
        when(kardexService.getMostRequestedTool()).thenReturn(dto);
        assertEquals(dto, kardexController.getMostRequestedTool());
        verify(kardexService).getMostRequestedTool();
    }
}
