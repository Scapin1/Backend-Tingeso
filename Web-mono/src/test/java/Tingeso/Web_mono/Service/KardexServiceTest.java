package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.LoansByMonthAndToolNameDTO;
import Tingeso.Web_mono.Controller.models.MostRequestedToolDTO;
import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KardexServiceTest {
    @Mock
    private KardexRepository kardexRepository;
    @InjectMocks
    private KardexService kardexService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(kardexRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.getAll());
        verify(kardexRepository).findAll();
    }

    @Test
    void testFindToolKardex() {
        when(kardexRepository.findByToolId(1L)).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.findToolKardex(1L));
        verify(kardexRepository).findByToolId(1L);
    }

    @Test
    void testFindBetweenDates() {
        when(kardexRepository.findBetweenDates(any(), any())).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.findBetweenDates(null, null));
        verify(kardexRepository).findBetweenDates(null, null);
    }

    @Test
    void testFindKardex_AllNull() {
        when(kardexRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.findKardex(null, null, null));
        verify(kardexRepository).findAll();
    }

    @Test
    void testCountLoansByMonthAndToolName() {
        when(kardexRepository.countLoansByMonthAndToolName()).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.countLoansByMonthAndToolName());
        verify(kardexRepository).countLoansByMonthAndToolName();
    }

    @Test
    void testGetMostRequestedTool_Empty() {
        when(kardexRepository.findMostRequestedTool()).thenReturn(Collections.emptyList());
        List<MostRequestedToolDTO> result = kardexService.getMostRequestedTool();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(kardexRepository).findMostRequestedTool();
    }

    @Test
    void testFindKardex_ByToolIdAndDates() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(kardexRepository.findByToolIdAndMovementDateBetween(1L, start, end)).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.findKardex(1L, start, end));
        verify(kardexRepository).findByToolIdAndMovementDateBetween(1L, start, end);
    }

    @Test
    void testFindKardex_ByToolIdOnly() {
        when(kardexRepository.findByToolId(1L)).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.findKardex(1L, null, null));
        verify(kardexRepository).findByToolId(1L);
    }

    @Test
    void testFindKardex_ByDatesOnly() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(kardexRepository.findBetweenDates(start, end)).thenReturn(Collections.emptyList());
        assertNotNull(kardexService.findKardex(null, start, end));
        verify(kardexRepository).findBetweenDates(start, end);
    }

    @Test
    void testGetMostRequestedTool_Valid() {
        MostRequestedToolDTO dto = new MostRequestedToolDTO("Taladro", 5L);
        List<MostRequestedToolDTO> resultList = Collections.singletonList(dto);
        when(kardexRepository.findMostRequestedTool()).thenReturn(resultList);
        List<MostRequestedToolDTO> result = kardexService.getMostRequestedTool();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Taladro", result.get(0).getToolName());
        assertEquals(5L, result.get(0).getRequestCount());
        verify(kardexRepository).findMostRequestedTool();
    }

    @Test
    void testGetMostRequestedTool_NullList() {
        when(kardexRepository.findMostRequestedTool()).thenReturn(null);
        List<MostRequestedToolDTO> result = kardexService.getMostRequestedTool();
        assertNull(result);
        verify(kardexRepository).findMostRequestedTool();
    }

    @Test
    void testGetMostRequestedToolInRange_Valid() {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        MostRequestedToolDTO dto = new MostRequestedToolDTO("Taladro", 5L);
        when(kardexRepository.findRequestedToolsInRangeDTO(start, end)).thenReturn(Collections.singletonList(dto));
        List<MostRequestedToolDTO> result = kardexService.getMostRequestedToolInRange(start, end);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Taladro", result.get(0).getToolName());
        verify(kardexRepository).findRequestedToolsInRangeDTO(start, end);
    }

    @Test
    void testGetMostRequestedToolInRange_Empty() {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        when(kardexRepository.findRequestedToolsInRangeDTO(start, end)).thenReturn(Collections.emptyList());
        List<MostRequestedToolDTO> result = kardexService.getMostRequestedToolInRange(start, end);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(kardexRepository).findRequestedToolsInRangeDTO(start, end);
    }

    @Test
    void testGetRequestedToolsInRange() {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        MostRequestedToolDTO dto = new MostRequestedToolDTO("Taladro", 5L);
        when(kardexRepository.findRequestedToolsInRangeDTO(start, end)).thenReturn(Collections.singletonList(dto));
        List<MostRequestedToolDTO> result = kardexService.getRequestedToolsInRange(start, end);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(kardexRepository).findRequestedToolsInRangeDTO(start, end);
    }
}
