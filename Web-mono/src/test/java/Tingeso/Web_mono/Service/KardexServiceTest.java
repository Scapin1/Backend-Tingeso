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
        assertNull(kardexService.getMostRequestedTool());
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
        Object[] row = new Object[]{"Taladro", 5L};
        List<Object[]> resultList = Collections.singletonList(row);
        when(kardexRepository.findMostRequestedTool()).thenReturn(resultList);
        MostRequestedToolDTO dto = kardexService.getMostRequestedTool();
        assertNotNull(dto);
        assertEquals("Taladro", dto.getToolName());
        assertEquals(5L, dto.getRequestCount());
        verify(kardexRepository).findMostRequestedTool();
    }

    @Test
    void testGetMostRequestedTool_NullList() {
        when(kardexRepository.findMostRequestedTool()).thenReturn(null);
        assertNull(kardexService.getMostRequestedTool());
        verify(kardexRepository).findMostRequestedTool();
    }

    @Test
    void testGetMostRequestedTool_NullValuesInRow() {
        Object[] row = new Object[]{null, null};
        List<Object[]> resultList = Collections.singletonList(row);
        when(kardexRepository.findMostRequestedTool()).thenReturn(resultList);
        MostRequestedToolDTO dto = kardexService.getMostRequestedTool();
        assertNotNull(dto);
        assertNull(dto.getToolName());
        assertEquals(0L, dto.getRequestCount());
        verify(kardexRepository).findMostRequestedTool();
    }
}
