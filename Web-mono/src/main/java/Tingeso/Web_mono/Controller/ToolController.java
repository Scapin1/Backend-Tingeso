package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Controller.models.ToolAvailableDTO;
import Tingeso.Web_mono.Entity.FeeEntity;
import Tingeso.Web_mono.Entity.ToolEntity;
import Tingeso.Web_mono.Service.ToolService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
public class ToolController {
    @Autowired
    private ToolService toolService;

    @GetMapping("/getAllList")
    public List<ToolAvailableDTO> getAllToolsList(HttpServletRequest request) {
        return toolService.findAllList(request);
    }

    @GetMapping("/getAll")
    public List<ToolEntity> getAllTools() {
        return toolService.findAll();
    }

    @PostMapping("/addTool")
    public void addTool(HttpServletRequest request) {
        toolService.save(request);
    }

    @PostMapping("/sendMaintenance")
    public ToolEntity sendMaintenance(HttpServletRequest request) {
        return toolService.sentMaintenance(request);
    }

    @PostMapping("/changeRepoFee")
    public FeeEntity changeRepoFee(HttpServletRequest request) {

        return toolService.changeRepoFee(request);
    }

    @PostMapping("/changeRentFee")
    public FeeEntity changeRentFee(HttpServletRequest request) {

        return toolService.changeRentFee(request);
    }

    @PostMapping("/changeLateFee")
    public FeeEntity changeLateFee(HttpServletRequest request) {
        return toolService.changeLateFee(request);
    }

    @PostMapping("/changeMaintenanceFee")
    public FeeEntity changeMaintenanceFee(HttpServletRequest request) {
        return toolService.changeMaintenanceFee(request);
    }

    @PutMapping("/writeOff")
    public ToolEntity writeOffTool(Long toolId) {
        return toolService.writeOff(toolId);
    }
}
