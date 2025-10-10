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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

}
