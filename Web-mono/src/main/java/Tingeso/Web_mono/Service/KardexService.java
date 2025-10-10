package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.LoansByMonthAndToolNameDTO;
import Tingeso.Web_mono.Controller.models.MostRequestedToolDTO;
import Tingeso.Web_mono.Entity.KardexEntity;
import Tingeso.Web_mono.Repository.KardexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class KardexService {
    private final KardexRepository kardexRepository;

    public List<KardexEntity> findToolKardex(Long toolId) {
        return kardexRepository.findByToolId(toolId);
    }

    public List<KardexEntity> findBetweenDates(LocalDateTime start, LocalDateTime end) {
        return kardexRepository.findBetweenDates(start, end);
    }

    public List<KardexEntity> getAll() {
        return kardexRepository.findAll();
    }

    public List<KardexEntity> findKardex(Long toolId, LocalDateTime start, LocalDateTime end) {
        if (toolId != null && start != null && end != null) {
            // Filtrar por herramienta y rango de fechas
            return kardexRepository.findByToolIdAndMovementDateBetween(toolId, start, end);
        } else if (toolId != null) {
            // Solo por herramienta
            return kardexRepository.findByToolId(toolId);
        } else if (start != null && end != null) {
            // Solo por rango de fechas
            return kardexRepository.findBetweenDates(start, end);
        } else {
            // Sin filtros, retorna todo
            return kardexRepository.findAll();
        }
    }

    public List<LoansByMonthAndToolNameDTO> countLoansByMonthAndToolName() {
        return kardexRepository.countLoansByMonthAndToolName();
    }

    public MostRequestedToolDTO getMostRequestedTool() {
        return kardexRepository.findMostRequestedTool();
    }


}
