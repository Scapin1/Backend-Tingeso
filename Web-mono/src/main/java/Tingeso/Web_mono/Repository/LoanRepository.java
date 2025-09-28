package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Controller.models.LoanDTO;
import Tingeso.Web_mono.Entity.ClientEntity;
import Tingeso.Web_mono.Entity.LoanEntity;
import Tingeso.Web_mono.Entity.LoanState;
import Tingeso.Web_mono.Entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("SELECT new Tingeso.Web_mono.Controller.models.LoanDTO(l.id, l.loanDate, l.returnDate, l.status, l.toolLoaned.name, l.client.rut) FROM LoanEntity l")
    List<LoanDTO> findAllLoan();

}
