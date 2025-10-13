package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Controller.models.ToolAvailableDTO;
import Tingeso.Web_mono.Entity.ToolEntity;
import Tingeso.Web_mono.Entity.ToolStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {
    ToolEntity findByName(String Name);

    @Query("SELECT COUNT(t) FROM ToolEntity t WHERE t.name = :toolName AND t.state = AVAILABLE")
    int getStock(String toolName);

    ToolEntity findTopByNameAndState(String name, ToolStateType state);

    @Query("SELECT new Tingeso.Web_mono.Controller.models.ToolAvailableDTO(t.name, t.category, COUNT(t), t.fee.repoFee, t.fee.maintenanceFee, t.fee.rentalFee, t.fee.lateFee) FROM ToolEntity t WHERE t.state = Tingeso.Web_mono.Entity.ToolStateType.AVAILABLE GROUP BY t.name, t.category, t.fee")
    List<ToolAvailableDTO> findAvailableToolsGrouped();


}
