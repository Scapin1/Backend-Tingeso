package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Controller.models.LoansByMonthAndToolNameDTO;
import Tingeso.Web_mono.Controller.models.MostRequestedToolDTO;
import Tingeso.Web_mono.Entity.KardexEntity;
import Tingeso.Web_mono.Service.KardexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kardex")
public class KardexController {

    private final KardexService kardexService;

    @GetMapping("/getAll")
    public List<KardexEntity> getAllKardex() {

        return kardexService.getAll();

    }

    @GetMapping("/filter")
    public List<KardexEntity> getFilteredKardex(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long toolId) {

        LocalDateTime start = (startDate != null && !startDate.isEmpty())
                ? LocalDateTime.parse(startDate)
                : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty())
                ? LocalDateTime.parse(endDate)
                : null;

        return kardexService.findKardex(toolId, start, end);
    }

    @GetMapping("/loansByMonthAndToolName")
    public List<LoansByMonthAndToolNameDTO> getLoansByMonthAndToolName() {
        return kardexService.countLoansByMonthAndToolName();
    }

    @GetMapping("/mostRequestedTool")
    public MostRequestedToolDTO getMostRequestedTool() {
        return kardexService.getMostRequestedTool();
    }

    @GetMapping("/mostRequestedToolInRange")
    public MostRequestedToolDTO getMostRequestedToolInRange(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return kardexService.getMostRequestedToolInRange(start, end);
    }

    @GetMapping("/requestedToolsInRange")
    public List<MostRequestedToolDTO> getRequestedToolsInRange(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return kardexService.getRequestedToolsInRange(start, end);
    }

}
