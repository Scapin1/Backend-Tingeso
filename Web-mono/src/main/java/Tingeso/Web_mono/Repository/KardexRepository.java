package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Entity.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Long> {

    List<KardexEntity> findByToolId(Long toolId);

    @Query("SELECT k FROM KardexEntity k WHERE k.movementDate BETWEEN :start AND :end")
    List<KardexEntity> findBetweenDates(LocalDateTime start, LocalDateTime end);
}
