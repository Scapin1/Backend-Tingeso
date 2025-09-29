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

    @GetMapping("/getFees/{toolId}")
    public FeeEntity getFees(@PathVariable Long toolId) {
        return toolService.getFees(toolId);
    }

    @PostMapping("/addTool")
    public void addTool(HttpServletRequest request) {
        toolService.save(request);
    }

    @PostMapping("/sendMaintenance/{toolId}")
    public ToolEntity sendMaintenance(@PathVariable Long toolId) {
        return toolService.sentMaintenance(toolId);
    }

    @PutMapping("/changeFee")
    public FeeEntity changeFee(@RequestBody FeeEntity fees) {
        return toolService.changeFee(fees);
    }

    @PutMapping("/writeOff")
    public ToolEntity writeOffTool(Long toolId) {
        return toolService.writeOff(toolId);
    }
}
