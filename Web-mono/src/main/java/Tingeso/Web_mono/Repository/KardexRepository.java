package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Controller.models.MostRequestedToolDTO;
import Tingeso.Web_mono.Entity.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Tingeso.Web_mono.Controller.models.LoansByMonthAndToolNameDTO;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Long> {

    List<KardexEntity> findByToolId(Long toolId);

    @Query("SELECT k FROM KardexEntity k WHERE k.movementDate BETWEEN :start AND :end")
    List<KardexEntity> findBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query("SELECT k FROM KardexEntity k WHERE k.toolId = :toolId AND k.movementDate BETWEEN :start AND :end")
    List<KardexEntity> findByToolIdAndMovementDateBetween(Long toolId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new Tingeso.Web_mono.Controller.models.LoansByMonthAndToolNameDTO(k.toolName, FUNCTION('MONTHNAME', k.movementDate), COUNT(k)) " +
            "FROM KardexEntity k " +
            "WHERE k.type = 'LOAN' " +
            "GROUP BY k.toolName, FUNCTION('MONTHNAME', k.movementDate)")
    List<LoansByMonthAndToolNameDTO> countLoansByMonthAndToolName();

    @Query("SELECT new Tingeso.Web_mono.Controller.models.MostRequestedToolDTO(k.toolName,COUNT(*)) " +
            "FROM KardexEntity k WHERE k.type = 'LOAN' " +
            "GROUP BY k.toolName ORDER BY COUNT(*) DESC LIMIT 1")
    MostRequestedToolDTO findMostRequestedTool();


}
