package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Entity.ToolEntity;
import Tingeso.Web_mono.Service.ToolService;
import Tingeso.Web_mono.Controller.models.CreateToolDTO;
import Tingeso.Web_mono.Controller.models.ToolAvailableDTO;
import Tingeso.Web_mono.Entity.FeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ToolControllerTest {
    @Mock
    private ToolService toolService;
    @InjectMocks
    private ToolController toolController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTools() {
        when(toolService.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(toolController.getAllTools());
        verify(toolService).findAll();
    }

    @Test
    void testGetAllTools_EmptyList() {
        when(toolService.findAll()).thenReturn(Collections.emptyList());
        assertTrue(toolController.getAllTools().isEmpty());
    }

    @Test
    void testAddTool() {
        CreateToolDTO dto = new CreateToolDTO();
        String username = "usuario";
        doNothing().when(toolService).save(dto, username);
        toolController.addTool(dto, username);
        verify(toolService).save(dto, username);
    }

    @Test
    void testGetAllToolsList() {
        when(toolService.findAllList()).thenReturn(Collections.emptyList());
        assertNotNull(toolController.getAllToolsList());
        verify(toolService).findAllList();
    }

    @Test
    void testGetFees() {
        FeeEntity fee = new FeeEntity();
        when(toolService.getFees(1L)).thenReturn(fee);
        assertEquals(fee, toolController.getFees(1L));
        verify(toolService).getFees(1L);
    }

    @Test
    void testSendMaintenance() {
        ToolEntity tool = new ToolEntity();
        when(toolService.sentMaintenance(1L, "user")).thenReturn(tool);
        assertEquals(tool, toolController.sendMaintenance(1L, "user"));
        verify(toolService).sentMaintenance(1L, "user");
    }

    @Test
    void testChangeFee() {
        FeeEntity fee = new FeeEntity();
        when(toolService.changeFee(fee)).thenReturn(fee);
        assertEquals(fee, toolController.changeFee(fee));
        verify(toolService).changeFee(fee);
    }

    @Test
    void testWriteOffTool() {
        ToolEntity tool = new ToolEntity();
        when(toolService.writeOff(1L, "user")).thenReturn(tool);
        assertEquals(tool, toolController.writeOffTool(1L, "user"));
        verify(toolService).writeOff(1L, "user");
    }
}
