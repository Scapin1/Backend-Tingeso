package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Controller.models.ClientWithMostLoansDTO;
import Tingeso.Web_mono.Controller.models.ClientWithMostOverduesDTO;
import Tingeso.Web_mono.Controller.models.LoanDTO;
import Tingeso.Web_mono.Controller.models.ToolWithMostOverduesDTO;
import Tingeso.Web_mono.Entity.ClientEntity;
import Tingeso.Web_mono.Entity.LoanEntity;
import Tingeso.Web_mono.Entity.LoanState;
import Tingeso.Web_mono.Entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    @Query("SELECT COUNT(l) > 0 FROM LoanEntity l WHERE l.toolLoaned.name = :toolName AND l.client = :client AND l.status = 'NORMAL'")
    boolean existsByToolAndClient(String toolName, ClientEntity client);

    @Query("SELECT COUNT(l) > 0 FROM LoanEntity l WHERE l.client.id = :clientId AND l.returnDate < CURRENT_TIMESTAMP AND l.status = 'NORMAL'")
    boolean existsOverdueLoanByClientId(Long clientId);

    long countByClientIdAndStatus(Long clientId, LoanState  status);

    @Query("SELECT l FROM LoanEntity l WHERE l.toolLoaned.id = :toolId AND l.status != Tingeso.Web_mono.Entity.LoanState.FINISHED")
    LoanEntity findByToolLoaned_Id(Long toolId);

    @Query("SELECT new Tingeso.Web_mono.Controller.models.LoanDTO(l.id, l.loanDate, l.returnDate, l.status, l.toolLoaned.name, l.toolLoaned.id, l.client.rut) FROM LoanEntity l WHERE (l.status != 'FINISHED' AND l.status != 'LATE_RETURN')")
    List<LoanDTO> findAllLoan();

    @Query("SELECT new Tingeso.Web_mono.Controller.models.ClientWithMostLoansDTO(l.client.rut, COUNT(l)) " +
            "FROM LoanEntity l GROUP BY l.client.rut ORDER BY COUNT(l) DESC LIMIT 5")
    List<ClientWithMostLoansDTO> findClientsWithMostLoans();

    @Query("SELECT new Tingeso.Web_mono.Controller.models.ClientWithMostOverduesDTO(l.client.rut, COUNT(l)) " +
            "FROM LoanEntity l WHERE l.status = 'OVERDUE' OR l.status = 'LATE_RETURN' " +
            "GROUP BY l.client.rut ORDER BY COUNT(l) DESC limit 5")
    List<ClientWithMostOverduesDTO> findClientsWithMostOverdues();

    @Modifying
    @Query("UPDATE LoanEntity l SET l.status = 'OVERDUE', l.LateStatus = TRUE WHERE l.returnDate < CURRENT_DATE AND l.status = 'NORMAL'")
    void markOverdueLoans();

    @Query(value = "SELECT new Tingeso.Web_mono.Controller.models.ToolWithMostOverduesDTO(t.name, COUNT(*)) " +
            "FROM LoanEntity l JOIN l.toolLoaned t " +
            "WHERE l.status = 'OVERDUE' OR l.status = 'LATE_RETURN' " +
            "GROUP BY t.name ORDER BY COUNT(*) DESC LiMIT 1")
    ToolWithMostOverduesDTO findToolWithMostOverdues();
}
