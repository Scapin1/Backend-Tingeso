package Tingeso.Web_mono.Service;

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
}
